package com.senai.todolist.api.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequestDto(
        @NotBlank(message = "The task name cannot be blank.")
        @Size(max = 100, message = "The task name must be a maximum of 100 characters.")
        String taskName,

        @Size(max = 255, message = "The description must be a maximum of 255 characters.")
        String taskDescription,

        @Min(value = 1, message = "The lowest priority is 1.")
        @Max(value = 5, message = "The highest priority is 5.")
        int priority
) {
}
