package com.senai.todolist.api.dto.user.login;

import com.senai.todolist.domain.model.Role;

import java.util.List;

public record RecoveryUserDto(
        Long id,
        String email,
        List<Role> roles
) {
}
