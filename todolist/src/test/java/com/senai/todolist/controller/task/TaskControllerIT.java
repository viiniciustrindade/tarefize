package com.senai.todolist.controller.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.api.dto.task.TaskPatchDto;
import com.senai.todolist.api.dto.task.TaskRequestDto;
import com.senai.todolist.domain.model.RoleName;
import com.senai.todolist.domain.model.Task;
import com.senai.todolist.domain.model.User;
import com.senai.todolist.infraecstruture.repository.FailedEventRepository;
import com.senai.todolist.infraecstruture.repository.TaskRepository;
import com.senai.todolist.infraecstruture.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private FailedEventRepository failedEventRepository;

    private User userDefault;

    @BeforeEach
    void setup() {

        taskRepository.deleteAll();
        userRepository.deleteAll();

        userDefault = new User("Vinicius", "vinicius@teste.com", "senha123");
        userDefault.setRole(RoleName.ROLE_USER);
        userRepository.save(userDefault);
    }

    @Test
    @DisplayName("Deve criar tarefa com sucesso (POST)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void criarTarefa() throws Exception {
        TaskRequestDto dto = new TaskRequestDto("Estudar Spring", "Testes de Integração", 5);

        mockMvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskName").value("Estudar Spring"))
                .andExpect(jsonPath("$.id").exists());

        assertEquals(1, taskRepository.count());
    }

    @Test
    @DisplayName("Deve retornar 503 mas garantir que a falha foi salva no banco")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deveCriarTarefaESalvarNoBancoDeFalhasQuandoKafkaCai() throws Exception {
        TaskRequestDto dto = new TaskRequestDto("Tarefa com Erro Kafka", "Testando Resiliência", 3);

        doThrow(new RuntimeException("Simulação de queda do Broker"))
                .when(kafkaTemplate).send(any(), any(), any());

        mockMvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isServiceUnavailable());
        var falhas = failedEventRepository.findAll();
        assertEquals(1, falhas.size(), "O evento deve ser salvo mesmo que o controller retorne erro");
        assertTrue(falhas.get(0).getPayload().contains("Tarefa com Erro Kafka"));
    }

    @Test
    @DisplayName("Deve listar tarefas do usuário logado (GET)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void listarTarefas() throws Exception {

        Task t = new Task("Tarefa Existente", "Desc", 3);
        t.setUser(userDefault);
        taskRepository.save(t);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].taskName").value("Tarefa Existente"));
    }

    @Test
    @DisplayName("Deve concluir tarefa com sucesso (PATCH)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void concluirTarefa() throws Exception {
        Task t = new Task("Incompleta", "Desc", 1);
        t.setUser(userDefault);
        taskRepository.save(t);

        mockMvc.perform(patch("/api/tasks/" + t.getId() + "/complete")
                        .with(csrf()))
                .andExpect(status().isOk());

        Task tarefaNoBanco = taskRepository.findById(t.getId()).get();
        assertTrue(tarefaNoBanco.isCompleted());
    }

    @Test
    @DisplayName("Deve atualizar campos da tarefa (PATCH)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void atualizarTarefa() throws Exception {
        Task t = new Task("Antigo", "Antigo", 1);
        t.setUser(userDefault);
        taskRepository.save(t);

        TaskPatchDto patch = new TaskPatchDto("Novo Nome", null, 5);

        mockMvc.perform(patch("/api/tasks/" + t.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Novo Nome"))
                .andExpect(jsonPath("$.priority").value(5));
    }

    @Test
    @DisplayName("Deve deletar tarefa (DELETE)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletarTarefa() throws Exception {
        Task t = new Task("Para Deletar", "Desc", 1);
        t.setUser(userDefault);
        taskRepository.save(t);

        mockMvc.perform(delete("/api/tasks/" + t.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertFalse(taskRepository.existsById(t.getId()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar concluir uma tarefa que não existe")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void concluirTarefaInexistente() throws Exception {

        mockMvc.perform(patch("/api/tasks/999/complete")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar uma tarefa que não existe")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletarTarefaInexistente() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/tasks/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 403 (Forbidden) ao acessar sem autenticação")
    void acessoSemAutenticacao() throws Exception {

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar ErrorResponse formatado quando tarefa não existir")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deveRetornar404ComCorpoFormatado() throws Exception {

        mockMvc.perform(patch("/api/tasks/999/complete")
                        .with(csrf()))
                .andExpect(status().isNotFound()) // O status vem do seu ErrorCode dentro da BusinessException
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("TAREFA_NAO_EXISTE"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve detalhar erros de validação no ErrorResponse")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deveRetornar400ComDetalhesDeValidacao() throws Exception {

        var dtoInvalido = new TaskRequestDto("", "Desc", 10);


        mockMvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))

                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("taskName")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("priority")));
    }
}