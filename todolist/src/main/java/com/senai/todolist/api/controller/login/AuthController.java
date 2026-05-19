package com.senai.todolist.api.controller.login;

import com.senai.todolist.api.dto.user.login.LoginUserDto;
import com.senai.todolist.api.dto.user.login.RecoveryJwtTokenDto;
import com.senai.todolist.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<RecoveryJwtTokenDto> login(
            @Valid @RequestBody LoginUserDto request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.userAuthenticate(request));
    }
}
