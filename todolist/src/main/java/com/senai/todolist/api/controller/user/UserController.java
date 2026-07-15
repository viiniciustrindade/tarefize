package com.senai.todolist.api.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senai.todolist.api.dto.user.FindUserByEmailRequestDto;
import com.senai.todolist.api.dto.user.register.UserResponseDto;
import com.senai.todolist.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<UserResponseDto>  findNameUserByEmail(
        @RequestBody FindUserByEmailRequestDto request
    ){
        return ResponseEntity.ok().body(userService.findNameUserByEmail(request));
    }
    
}
