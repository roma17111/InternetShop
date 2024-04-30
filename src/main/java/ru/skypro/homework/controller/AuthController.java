package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.SecurityContextLoginModule;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.LoginReq;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;

import static ru.skypro.homework.dto.Role.USER;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 */
@Log4j2
@CrossOrigin(value = {"*"})
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Авторизация пользователя.
     *
     * @param req объект запроса с данными пользователя
     * @return ResponseEntity с результатом авторизации
     */
    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    @ApiResponse(responseCode = "200",description = "OK")
    @ApiResponse(responseCode = "404",description = "Not Found")
    @ApiResponse(responseCode = "401",description = "Unauthorized")
    @ApiResponse(responseCode = "403",description = "Forbidden")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        if (authService.login(req.getUsername(), req.getPassword())) {
            log.info("logining user " + authService.getByUserName(req.getUsername()));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Регистрация пользователя.
     *
     * @param req объект запроса с данными для регистрации пользователя
     * @return ResponseEntity с результатом регистрации
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "404",description = "Not Found")
    @ApiResponse(responseCode = "201",description = "Created")
    @ApiResponse(responseCode = "401",description = "Unauthorized")
    @ApiResponse(responseCode = "403",description = "Forbidden")
    public ResponseEntity<?> register(@RequestBody RegisterReqDto req) {
        Role role = req.getRole() == null ? USER : req.getRole();
        if (authService.register(req, role)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
