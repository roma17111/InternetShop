package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class UserController {


    private final AuthService authService;

    @PatchMapping("/users/me")
    public UserDto updateUser(@RequestBody UserDto user) {
        System.out.println("Hello");
        return new UserDto(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone());
    }

    @GetMapping("/users/me")
    public UserDto getUser() {
        UserDto userDto = authService.getUser();
        System.out.println(userDto);
        return new UserDto(userDto.getEmail(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getPhone());
    }
}

