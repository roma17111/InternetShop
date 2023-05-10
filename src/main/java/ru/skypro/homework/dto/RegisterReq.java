package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс-данных (DTO) для передачи информации о регистрации пользователя.
 */
@Data
public class RegisterReq {
    /**
     * Имя пользователя (логин).
     */
    private String username;
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Имя пользователя.
     */
    private String firstName;
    /**
     * Фамилия пользователя.
     */
    private String lastName;
    /**
     * Телефонный номер пользователя.
     */
    private String phone;
    /**
     * Роль пользователя в системе (по умолчанию Role.USER).
     */
    private Role role = Role.USER;
}