package com.senai.todolist.api.mapper;

import com.senai.todolist.api.dto.task.TaskRequestDto;
import com.senai.todolist.api.dto.task.TaskResponseDto;
import com.senai.todolist.domain.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequestDto requestDto) {
        return new Task(requestDto.taskName(), requestDto.taskDescription(), requestDto.priority());
    }

    public TaskResponseDto toResponseDto(Task task) {
        return new TaskResponseDto(task.getId(),
                task.getTaskName(),
                task.getTaskDescription(),
                task.getPriority(),
                task.getCreationDate());
    }
}
