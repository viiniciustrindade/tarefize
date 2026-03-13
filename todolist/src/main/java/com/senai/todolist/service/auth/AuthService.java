package com.senai.todolist.service.auth;

import com.senai.todolist.api.dto.usuario.login.LoginUserDto;
import com.senai.todolist.api.dto.usuario.login.RecoveryJwtTokenDto;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    public RecoveryJwtTokenDto autenticarUsuario(
            LoginUserDto userDto
    ){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.senha());

        Authentication authentication = authenticationManager.authenticate(token);

        Usuario usuario = (Usuario) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(usuario));
    }
}
