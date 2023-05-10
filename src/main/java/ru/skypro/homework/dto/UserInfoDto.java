package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) класс для передачи информации о пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    /**
     * ID пользователя.
     */
    private long id;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Телефон пользователя.
     */
    private String phone;

    /**
     * Дата регистрации пользователя.
     * Не передаётся в JSON благодаря аннотации @JsonIgnore.
     */
    @JsonIgnore
    private String regDate = String.valueOf(LocalDateTime.now());

    /**
     * URL изображения пользователя.
     */
    private String image = "/users/image";

    /**
     * Конструктор для создания DTO с именем, фамилией и телефоном пользователя.
     */
    public UserInfoDto(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    /**
     * Конструктор для создания DTO с электронной почтой, именем, фамилией и телефоном пользователя.
     */
    public UserInfoDto(String email, String firstName, String lastName, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    /**
     * Конструктор для создания DTO с ID, электронной почтой, именем, фамилией и телефоном пользователя.
     */
    public UserInfoDto(long id, String email, String firstName, String lastName, String phone) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

}
