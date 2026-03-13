package com.senai.todolist.controller.tarefa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.api.dto.tarefa.TarefaPatchDto;
import com.senai.todolist.api.dto.tarefa.TarefaRequisicaoDto;
import com.senai.todolist.domain.model.RoleName;
import com.senai.todolist.domain.model.Tarefa;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.repository.FailedEventRepository;
import com.senai.todolist.infraecstruture.repository.TarefaRepository;
import com.senai.todolist.infraecstruture.repository.UsuarioRepository;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
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
class TarefaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @MockitoSpyBean
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private FailedEventRepository failedEventRepository;

    private Usuario usuarioPadrao;

    @BeforeEach
    void setup() {

        tarefaRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuarioPadrao = new Usuario("Vinicius", "vinicius@teste.com", "senha123");
        usuarioPadrao.setRole(RoleName.ROLE_USER);
        usuarioRepository.save(usuarioPadrao);
    }

    @Test
    @DisplayName("Deve criar tarefa com sucesso (POST)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void criarTarefa() throws Exception {
        TarefaRequisicaoDto dto = new TarefaRequisicaoDto("Estudar Spring", "Testes de Integração", 5);

        mockMvc.perform(post("/api/tarefas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeTarefa").value("Estudar Spring"))
                .andExpect(jsonPath("$.id").exists());

        assertEquals(1, tarefaRepository.count());
    }

    @Test
    @DisplayName("Deve retornar 503 mas garantir que a falha foi salva no banco")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deveCriarTarefaESalvarNoBancoDeFalhasQuandoKafkaCai() throws Exception {
        // Arrange
        TarefaRequisicaoDto dto = new TarefaRequisicaoDto("Tarefa com Erro Kafka", "Testando Resiliência", 3);

        doThrow(new RuntimeException("Simulação de queda do Broker"))
                .when(kafkaTemplate).send(anyString(), anyString(), any());

        // Act
        mockMvc.perform(post("/api/tarefas")
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

        Tarefa t = new Tarefa("Tarefa Existente", "Desc", 3);
        t.setUsuario(usuarioPadrao);
        tarefaRepository.save(t);

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeTarefa").value("Tarefa Existente"));
    }

    @Test
    @DisplayName("Deve concluir tarefa com sucesso (PATCH)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void concluirTarefa() throws Exception {
        Tarefa t = new Tarefa("Incompleta", "Desc", 1);
        t.setUsuario(usuarioPadrao);
        tarefaRepository.save(t);

        mockMvc.perform(patch("/api/tarefas/" + t.getId() + "/concluir")
                        .with(csrf()))
                .andExpect(status().isOk());

        Tarefa tarefaNoBanco = tarefaRepository.findById(t.getId()).get();
        assertTrue(tarefaNoBanco.isConcluida());
    }

    @Test
    @DisplayName("Deve atualizar campos da tarefa (PATCH)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void atualizarTarefa() throws Exception {
        Tarefa t = new Tarefa("Antigo", "Antigo", 1);
        t.setUsuario(usuarioPadrao);
        tarefaRepository.save(t);

        TarefaPatchDto patch = new TarefaPatchDto("Novo Nome", null, 5);

        mockMvc.perform(patch("/api/tarefas/" + t.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTarefa").value("Novo Nome"))
                .andExpect(jsonPath("$.prioridade").value(5));
    }

    @Test
    @DisplayName("Deve deletar tarefa (DELETE)")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletarTarefa() throws Exception {
        Tarefa t = new Tarefa("Para Deletar", "Desc", 1);
        t.setUsuario(usuarioPadrao);
        tarefaRepository.save(t);

        mockMvc.perform(delete("/api/tarefas/" + t.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertFalse(tarefaRepository.existsById(t.getId()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar concluir uma tarefa que não existe")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void concluirTarefaInexistente() throws Exception {

        mockMvc.perform(patch("/api/tarefas/999/concluir")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar uma tarefa que não existe")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletarTarefaInexistente() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/tarefas/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 403 (Forbidden) ao acessar sem autenticação")
    void acessoSemAutenticacao() throws Exception {

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar ErrorResponse formatado quando tarefa não existir")
    @WithUserDetails(value = "vinicius@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deveRetornar404ComCorpoFormatado() throws Exception {

        mockMvc.perform(patch("/api/tarefas/999/concluir")
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

        var dtoInvalido = new TarefaRequisicaoDto("", "Desc", 10);


        mockMvc.perform(post("/api/tarefas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))

                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("nome da tarefa")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("prioridade")));
    }
}