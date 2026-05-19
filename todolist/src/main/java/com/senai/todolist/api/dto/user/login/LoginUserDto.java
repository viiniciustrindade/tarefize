package com.senai.todolist.api.dto.user.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(
        @NotBlank(message = "E-mail is mandatory!")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "password is mandatory!")
        String password
) {
}
