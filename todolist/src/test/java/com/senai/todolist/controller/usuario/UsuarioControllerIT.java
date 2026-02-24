package com.senai.todolist.controller.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.todolist.domain.dto.usuario.cadastro.UsuarioRequisicaoDto;
import com.senai.todolist.infraecstruture.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve registrar um novo usuário e retornar um Token JWT")
    void deveRegistrarUsuarioComSucesso() throws Exception {

        var requisicao = new UsuarioRequisicaoDto("Novo Usuário", "novo@email.com", "senha123");


        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requisicao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());

        assertTrue(usuarioRepository.findByEmail("novo@email.com").isPresent());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar registrar usuário com e-mail inválido")
    void registrarUsuarioEmailInvalido() throws Exception {

        var requisicaoInvalida = new UsuarioRequisicaoDto("Nome", "email-invalido", "123");


        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requisicaoInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}