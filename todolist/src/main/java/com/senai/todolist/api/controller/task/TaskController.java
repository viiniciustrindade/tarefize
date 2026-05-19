package com.senai.todolist.api.controller.task;

import com.senai.todolist.api.dto.task.TaskPatchDto;
import com.senai.todolist.api.dto.task.TaskRequestDto;
import com.senai.todolist.api.dto.task.TaskResponseDto;
import com.senai.todolist.domain.model.User;
import com.senai.todolist.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(
            @Valid @RequestBody TaskRequestDto requestDto,
            @AuthenticationPrincipal User user
    ){
        TaskResponseDto response =
                taskService.createTask(user,requestDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> findAllTasks(
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal User user
    ){
        Page<TaskResponseDto> page =
                taskService.findAllTasks(user,pageable);
            return ResponseEntity.ok(page);
    }

    @PatchMapping("/{idTask}")
    public ResponseEntity<TaskResponseDto> updateTaskById(
            @PathVariable Long idTask,
            @Valid@RequestBody TaskPatchDto taskRequestDto,
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(
                taskService.updateTask(
                        user,
                        idTask,
                        taskRequestDto));
    }

    @DeleteMapping("/{idTask}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long idTask,
            @AuthenticationPrincipal User user
    ){
        taskService.deleteTaskById(user,idTask);
        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{idTask}/complete")
    public ResponseEntity<Void> completeTask(
            @PathVariable Long idTask,
            @AuthenticationPrincipal User user
    ){
        taskService.completeTask(idTask,user);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{idTask}")
    public ResponseEntity<TaskResponseDto> buscarTarefaPorId(
            @PathVariable Long idTask,
            @AuthenticationPrincipal User user
    ){
         return ResponseEntity.ok(taskService.findTaskById(idTask, user));
    }
}
