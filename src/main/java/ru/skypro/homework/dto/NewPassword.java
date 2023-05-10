package ru.skypro.homework.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

/**
 * Класс представляющий DTO (Data Transfer Object) для смены пароля пользователя.
 * Используется для передачи данных между слоями приложения.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewPassword {

    /**
     * Текущий пароль пользователя.
     */
    @NotBlank
    String currentPassword;

    /**
     * Новый пароль пользователя.
     */
    @NotBlank
    String newPassword;
}
