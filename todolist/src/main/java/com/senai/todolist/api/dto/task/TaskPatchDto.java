package com.senai.todolist.api.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskPatchDto(
        String taskName,
        String taskDescription,
        Integer priority
) {
}
