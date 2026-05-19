package com.senai.todolist.api.dto.user.register;

public record UserResponseDto(
        Long id,
        String nome,
        String email
) {
}
