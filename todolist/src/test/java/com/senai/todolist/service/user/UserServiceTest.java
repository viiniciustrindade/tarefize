package com.senai.todolist.service.user;

import com.senai.todolist.api.dto.user.register.UserRequestDto;
import com.senai.todolist.domain.model.User;
import com.senai.todolist.infraecstruture.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve salvar usuário com a senha criptografada")
    void registrarUsuarioComSucesso() {

        var dto = new UserRequestDto("Vinicius", "vini@email.com", "senha123");
        String senhaCriptografada = "hash_encriptado_abc";

        when(passwordEncoder.encode(dto.password())).thenReturn(senhaCriptografada);

        userService.registerUser(dto);

        verify(passwordEncoder, times(1)).encode("senha123");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Vinicius", savedUser.getName());
        assertEquals("vini@email.com", savedUser.getEmail());
        assertEquals(senhaCriptografada, savedUser.getPassword());
    }

    @Test
    @DisplayName("Deve propagar exceção quando o repositório falhar")
    void registrarUsuarioErroBanco() {

        var dto = new UserRequestDto("Vini", "vini@email.com", "senha12345");

        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        doThrow(new RuntimeException("Erro de conexão")).when(userRepository).save(any(User.class));

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });
    }
}