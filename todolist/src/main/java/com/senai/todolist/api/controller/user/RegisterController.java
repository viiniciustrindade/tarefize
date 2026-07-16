package com.senai.todolist.api.controller.user;

import com.senai.todolist.api.dto.user.register.UserRequestDto;
import com.senai.todolist.api.dto.user.login.LoginUserDto;
import com.senai.todolist.api.dto.user.login.RecoveryJwtTokenDto;
import com.senai.todolist.service.auth.AuthService;
import com.senai.todolist.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@AllArgsConstructor
public class RegisterController {
    private final UserService userService;

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<RecoveryJwtTokenDto> createUser(
            @Valid @RequestBody UserRequestDto resquest
    ){
        userService.registerUser(resquest);

        LoginUserDto loginUserDto = new LoginUserDto(
                resquest.email(),
                resquest.password()
        );

        RecoveryJwtTokenDto token = authService.userAuthenticate(loginUserDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(token);
    }
}
