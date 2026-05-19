package com.senai.todolist.api.dto.task;

import java.time.LocalDate;

public record TaskResponseDto(
        Long id,
        String taskName,
        String taskDescription,
        int priority,
        LocalDate createDate
) {
}
