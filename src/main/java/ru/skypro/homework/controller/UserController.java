package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class UserController {


    private final AuthService authService;

    @PatchMapping("/users/me")
    public UserDto updateUser(@RequestBody UserInfo userInfo) {
        System.out.println("Hello");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        System.out.println(authentication.isAuthenticated());
        return new UserDto(
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhone());
    }


    @GetMapping("/users/me")
    public UserInfo getUser() {
    //    UserDto userDto = authService.getUser();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        System.out.println(authentication.isAuthenticated());
        return new UserInfo("Test", "test", "12345");
    }

}

