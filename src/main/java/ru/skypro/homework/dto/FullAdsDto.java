package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Класс представляющий DTO (Data Transfer Object) для полных объявлений.
 * Используется для передачи данных между слоями приложения.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class FullAdsDto {

    /**
     * Идентификатор объявления.
     */
    long pk;

    /**
     * Имя автора объявления.
     */
    String authorFirstName;

    /**
     * Фамилия автора объявления.
     */
    String authorLastName;

    /**
     * Описание объявления.
     */
    String description;

    /**
     * Email автора объявления.
     */
    String email;

    /**
     * URL-ссылка на изображение объявления.
     */
    String image;

    /**
     * Номер телефона автора объявления.
     */
    String phone;

    /**
     * Цена, указанная в объявлении.
     */
    int price;

    /**
     * Заголовок объявления.
     */
    String title;

}
