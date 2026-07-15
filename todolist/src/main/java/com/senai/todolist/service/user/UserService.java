package com.senai.todolist.service.user;

import com.senai.todolist.api.dto.user.FindUserByEmailRequestDto;
import com.senai.todolist.api.dto.user.register.UserRequestDto;
import com.senai.todolist.api.dto.user.register.UserResponseDto;
import com.senai.todolist.api.mapper.UserMapper;
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

    private final UserMapper userMapper;

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

    public UserResponseDto findNameUserByEmail(
        FindUserByEmailRequestDto request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        return userMapper.toResponseDto(user);
    }
}
