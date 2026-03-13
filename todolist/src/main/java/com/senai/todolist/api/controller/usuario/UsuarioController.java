package com.senai.todolist.api.controller.usuario;

import com.senai.todolist.api.dto.usuario.cadastro.UsuarioRequisicaoDto;
import com.senai.todolist.api.dto.usuario.login.LoginUserDto;
import com.senai.todolist.api.dto.usuario.login.RecoveryJwtTokenDto;
import com.senai.todolist.service.auth.AuthService;
import com.senai.todolist.service.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<RecoveryJwtTokenDto> registarUsuario(
            @Valid @RequestBody UsuarioRequisicaoDto requisicao
    ){
        usuarioService.registrarUsuario(requisicao);

        LoginUserDto loginUserDto = new LoginUserDto(
                requisicao.email(),
                requisicao.senha()
        );

        RecoveryJwtTokenDto token = authService.autenticarUsuario(loginUserDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(token);
    }
}
