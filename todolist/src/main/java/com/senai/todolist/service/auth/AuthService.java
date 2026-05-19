package com.senai.todolist.service.auth;

import com.senai.todolist.api.dto.user.login.LoginUserDto;
import com.senai.todolist.api.dto.user.login.RecoveryJwtTokenDto;
import com.senai.todolist.domain.model.User;
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

    public RecoveryJwtTokenDto userAuthenticate(
            LoginUserDto userDto
    ){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password());

        Authentication authentication = authenticationManager.authenticate(token);

        User usuario = (User) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(usuario));
    }
}
