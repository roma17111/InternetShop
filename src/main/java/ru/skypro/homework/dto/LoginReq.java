package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс-данных (DTO) для передачи информации о логине пользователя.
 */
@Data
public class LoginReq {
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Имя пользователя (логин).
     */
    private String username;
}}
