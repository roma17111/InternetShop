package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Table;

/**
 * Класс представляющий DTO (Data Transfer Object) объявлений.
 * Используется для передачи данных между слоями приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "ads_test")
public class AdsDto {

    /**
     * Идентификатор автора объявления.
     */
    long author;

    /**
     * Ссылка на изображение объявления.
     */
    String image;

    /**
     * Уникальный идентификатор объявления.
     */
    long pk;

    /**
     * Цена указанная в объявлении.
     */
    int price;

    /**
     * Заголовок объявления.
     */
    String title;

}