package com.senai.todolist.service.user;

import com.senai.todolist.api.dto.user.register.UserRequestDto;
import com.senai.todolist.domain.exception.EmailJaCadastradoException;
import com.senai.todolist.domain.model.User;
import com.senai.todolist.infraecstruture.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserRequestDto userRequestDto) {
        validarEmailUnico(userRequestDto.email());

        User user = new User(
                userRequestDto.name(),
                userRequestDto.email(),
                passwordEncoder.encode(userRequestDto.password())
        );

        userRepository.save(user);
    }


    private void validarEmailUnico(String email){
        if(userRepository.existsByEmail(email)){
            throw new EmailJaCadastradoException();
        }
    }
}
