package com.senai.todolist.service.task;

import com.senai.todolist.api.dto.task.TaskPatchDto;
import com.senai.todolist.api.dto.task.TaskRequestDto;
import com.senai.todolist.api.dto.task.TaskResponseDto;
import com.senai.todolist.domain.exception.TarefaNãoExisteException;
import com.senai.todolist.api.mapper.TaskMapper;
import com.senai.todolist.domain.model.Task;
import com.senai.todolist.domain.model.User;
import com.senai.todolist.infraecstruture.repository.TaskRepository;
import com.senai.todolist.service.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private final NotificationService notificationService;

    private final TaskMapper taskMapper;

    public TaskResponseDto createTask(User user,
                                      TaskRequestDto taskRequestDto){
        Task task = taskMapper.toEntity(taskRequestDto);
        task.setUser(user);

        taskRepository.save(task);

        NotificationEvent notificacao = new NotificationEvent(
                user.getEmail(),
                "EMAIL",
                "TAREFA_CRIADA",
                Map.of(
                    "nomeTarefa", task.getTaskName(),
                    "descricao", task.getTaskDescription(),
                    "dataCriacao", LocalDateTime.now().toString()
                )
        );

        notificationService.enviar(notificacao);

        return taskMapper.toResponseDto(task);
    }

    public Page<TaskResponseDto> findAllTasks(
            User usuario, Pageable pageable) {

        Page<Task> pagina = taskRepository.findByUser(usuario,pageable);

        return pagina.map(taskMapper::toResponseDto);
    }

    public TaskResponseDto updateTask(
            User user, Long taskId,
            TaskPatchDto patchDto) {

        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new TarefaNãoExisteException(taskId));

        if (patchDto.taskName() != null) {
            task.setTaskName(patchDto.taskName());
        }
        if (patchDto.taskDescription() != null) {
            task.setTaskDescription(patchDto.taskDescription());
        }
        if (patchDto.priority() != null) {
            task.setPriority(patchDto.priority());
        }

        taskRepository.save(task);
        return taskMapper.toResponseDto(task);
    }

    public void deleteTaskById(User user, Long taskId) {
        if(!taskRepository.existsByIdAndUser(taskId,user)){
            throw new TarefaNãoExisteException(taskId);
        }

        taskRepository.deleteById(taskId);
    }

    public void completeTask(
            Long taskId, User user  ){
        Task tarefa = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new TarefaNãoExisteException(taskId));

        tarefa.setCompleted(true);
        taskRepository.save(tarefa);
    }

    public TaskResponseDto findTaskById(Long taskId, User user) {
        Task tarefa = taskRepository.findByIdAndUser(taskId,user)
                .orElseThrow(() -> new TarefaNãoExisteException(taskId));

        return taskMapper.toResponseDto(tarefa);
    }
}
