package com.senai.todolist.service.task;

import com.senai.todolist.api.dto.task.TaskRequestDto;
import com.senai.todolist.api.dto.task.TaskResponseDto;
import com.senai.todolist.domain.exception.TarefaNãoExisteException;
import com.senai.todolist.api.mapper.TaskMapper;
import com.senai.todolist.domain.model.Task;
import com.senai.todolist.domain.model.User;
import com.senai.todolist.infraecstruture.repository.TaskRepository;
import com.senai.todolist.service.event.NotificationEvent;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private NotificationService norificationService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private TaskRequestDto taskRequestDto;
    private TaskResponseDto responseDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);

        task = new Task("Estudar Testes", "Aprender Mockito", 5);
        task.setId(10L);
        task.setUser(user);

        taskRequestDto = new TaskRequestDto("Estudar Testes", "Aprender Mockito", 5);

        responseDto = new TaskResponseDto(10L, "Estudar Testes", "Aprender Mockito", 5, LocalDate.now());
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void deveCreateTaskComSucesso() {

        when(taskMapper.toEntity(any())).thenReturn(task);
        when(taskRepository.save(any())).thenReturn(task);
        when(taskMapper.toResponseDto(any())).thenReturn(responseDto);


        TaskResponseDto result = taskService.createTask(user, taskRequestDto);


        assertNotNull(result);
        assertEquals(responseDto.taskName(), result.taskName());
        verify(taskRepository, times(1)).save(task);
        verify(norificationService, times(1)).enviar(any(NotificationEvent.class));
        assertEquals(user, task.getUser());
    }

    @Test
    @DisplayName("Deve concluir uma tarefa com sucesso")
    void deveConcluirTarefaComSucesso() {

        when(taskRepository.findByIdAndUser(10L, user)).thenReturn(Optional.of(task));


        taskService.completeTask(10L, user);

        assertTrue(task.isCompleted());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar concluir tarefa inexistente")
    void deveLancarExcecaoQuandoTarefaNaoExiste() {
        // Arrange
        when(taskRepository.findByIdAndUser(anyLong(), any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TarefaNãoExisteException.class, () -> {
            taskService.completeTask(1L, user);
        });

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar uma tarefa com sucesso")
    void deveDeletarTarefaComSucesso() {
        when(taskRepository.existsByIdAndUser(10L, user)).thenReturn(true);

        taskService.deleteTaskById(user, 10L);

        verify(taskRepository, times(1)).deleteById(10L);
    }
}