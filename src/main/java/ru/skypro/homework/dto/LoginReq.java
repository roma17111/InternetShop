package ru.skypro.homework.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

/**
 * Класс представляющий DTO (Data Transfer Object) для запроса аутентификации.
 * Используется для передачи данных между слоями приложения.
 */
@Data
public class LoginReq {

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Имя пользователя или электронная почта.
     */
    private String username;

}