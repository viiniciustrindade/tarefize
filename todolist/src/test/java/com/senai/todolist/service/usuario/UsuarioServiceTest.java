package com.senai.todolist.service.usuario;

import com.senai.todolist.api.dto.usuario.cadastro.UsuarioRequisicaoDto;
import com.senai.todolist.domain.model.Usuario;
import com.senai.todolist.infraecstruture.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve salvar usuário com a senha criptografada")
    void registrarUsuarioComSucesso() {

        var dto = new UsuarioRequisicaoDto("Vinicius", "vini@email.com", "senha123");
        String senhaCriptografada = "hash_encriptado_abc";

        when(passwordEncoder.encode(dto.senha())).thenReturn(senhaCriptografada);

        usuarioService.registrarUsuario(dto);

        verify(passwordEncoder, times(1)).encode("senha123");

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario usuarioSalvo = usuarioCaptor.getValue();

        assertEquals("Vinicius", usuarioSalvo.getNome());
        assertEquals("vini@email.com", usuarioSalvo.getEmail());
        assertEquals(senhaCriptografada, usuarioSalvo.getSenha());
    }

    @Test
    @DisplayName("Deve propagar exceção quando o repositório falhar")
    void registrarUsuarioErroBanco() {

        var dto = new UsuarioRequisicaoDto("Vini", "vini@email.com", "senha12345");

        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        doThrow(new RuntimeException("Erro de conexão")).when(usuarioRepository).save(any(Usuario.class));

        assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(dto);
        });
    }
}