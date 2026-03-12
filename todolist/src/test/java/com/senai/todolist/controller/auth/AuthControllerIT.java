package com.senai.todolist.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.api.dto.usuario.login.LoginUserDto;
import com.senai.todolist.domain.model.RoleName;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario("Vini", "vini@teste.com", passwordEncoder.encode("senha123"));
        usuario.setRole(RoleName.ROLE_USER);
        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve logar com sucesso e receber status 200")
    void loginSucesso() throws Exception {
        var loginDto = new LoginUserDto("vini@teste.com", "senha123");

        mockMvc.perform(post("/api/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Deve retornar 401 (Unauthorized) ao errar a senha")
    void loginSenhaIncorreta() throws Exception {
        var loginDto = new LoginUserDto("vini@teste.com", "senha_errada");

        mockMvc.perform(post("/api/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 400 ao enviar e-mail em formato inválido")
    void loginEmailInvalido() throws Exception {
        var loginDto = new LoginUserDto("email_sem_arroba", "123456");

        mockMvc.perform(post("/api/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
