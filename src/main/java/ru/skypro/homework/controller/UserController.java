package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<User> getUser(@RequestBody User user) {
        return ResponseEntity.ok().build();
    }
}
