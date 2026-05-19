package com.senai.todolist.api.mapper;

import com.senai.todolist.api.dto.user.register.UserRequestDto;
import com.senai.todolist.api.dto.user.register.UserResponseDto;
import com.senai.todolist.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequestDto requestDto){
        return new User(requestDto.name(), requestDto.password());
    }

    public User toUpdateDto(UserRequestDto requestDto, User user){
        user.setName(requestDto.name());
        user.setPassword(requestDto.password());
        return user;
    }

    public UserResponseDto toResponseDto(User user){
        return new UserResponseDto(user.getId(),
                user.getName(),
                user.getEmail());
    }
}
