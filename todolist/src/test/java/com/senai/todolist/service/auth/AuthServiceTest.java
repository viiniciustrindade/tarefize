package com.senai.todolist.service.auth;

import com.senai.todolist.api.dto.usuario.login.LoginUserDto;
import com.senai.todolist.api.dto.usuario.login.RecoveryJwtTokenDto;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.security.JwtTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve autenticar usuário e retornar token com sucesso")
    void autenticarComSucesso() {

        var loginDto = new LoginUserDto("vini@teste.com", "12345678");
        var usuarioMock = new Usuario("Vinicius", "vini@teste.com", "senha_hash");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuarioMock);
        when(authenticationManager.authenticate(any())).thenReturn(authMock);
        when(jwtTokenService.generateToken(usuarioMock)).thenReturn("token-jwt-gerado");

        RecoveryJwtTokenDto resultado = authService.autenticarUsuario(loginDto);

        assertNotNull(resultado);
        assertEquals("token-jwt-gerado", resultado.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Deve propagar erro quando as credenciais forem inválidas")
    void autenticarComErro() {

        var loginDto = new LoginUserDto("errado@teste.com", "senha_errada");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Usuário ou senha inválidos"));

        assertThrows(BadCredentialsException.class, () -> authService.autenticarUsuario(loginDto));
    }
}