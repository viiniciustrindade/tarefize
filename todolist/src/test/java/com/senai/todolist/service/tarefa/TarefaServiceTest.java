package com.senai.todolist.service.tarefa;

import com.senai.todolist.domain.dto.tarefa.TarefaRequisicaoDto;
import com.senai.todolist.domain.dto.tarefa.TarefaRespostaDto;
import com.senai.todolist.domain.exception.TarefaNãoExisteException;
import com.senai.todolist.domain.mapper.TarefaMapper;
import com.senai.todolist.domain.model.Tarefa;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaMapper tarefaMapper;

    @InjectMocks
    private TarefaService tarefaService;

    private Usuario usuario;
    private Tarefa tarefa;
    private TarefaRequisicaoDto requisicaoDto;
    private TarefaRespostaDto respostaDto;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);

        tarefa = new Tarefa("Estudar Testes", "Aprender Mockito", 5);
        tarefa.setId(10L);
        tarefa.setUsuario(usuario);

        requisicaoDto = new TarefaRequisicaoDto("Estudar Testes", "Aprender Mockito", 5);

        respostaDto = new TarefaRespostaDto(10L, "Estudar Testes", "Aprender Mockito", 5, LocalDate.now());
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void deveCriarTarefaComSucesso() {

        when(tarefaMapper.toEntity(any())).thenReturn(tarefa);
        when(tarefaRepository.save(any())).thenReturn(tarefa);
        when(tarefaMapper.toRespostaDto(any())).thenReturn(respostaDto);


        TarefaRespostaDto resultado = tarefaService.criarTarefa(usuario, requisicaoDto);


        assertNotNull(resultado);
        assertEquals(respostaDto.nomeTarefa(), resultado.nomeTarefa());
        verify(tarefaRepository, times(1)).save(tarefa);
        assertEquals(usuario, tarefa.getUsuario());
    }

    @Test
    @DisplayName("Deve concluir uma tarefa com sucesso")
    void deveConcluirTarefaComSucesso() {

        when(tarefaRepository.findByIdAndUsuario(10L, usuario)).thenReturn(Optional.of(tarefa));


        tarefaService.concluirTarefa(10L, usuario);

        assertTrue(tarefa.isConcluida());
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar concluir tarefa inexistente")
    void deveLancarExcecaoQuandoTarefaNaoExiste() {
        // Arrange
        when(tarefaRepository.findByIdAndUsuario(anyLong(), any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TarefaNãoExisteException.class, () -> {
            tarefaService.concluirTarefa(1L, usuario);
        });

        verify(tarefaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar uma tarefa com sucesso")
    void deveDeletarTarefaComSucesso() {
        when(tarefaRepository.existsByIdAndUsuario(10L, usuario)).thenReturn(true);

        tarefaService.deletarTarefaPorId(usuario, 10L);

        verify(tarefaRepository, times(1)).deleteById(10L);
    }
}