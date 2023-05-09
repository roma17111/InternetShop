package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Класс представляющий DTO (Data Transfer Object) комментариев.
 * Используется для передачи данных между слоями приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    /**
     * Идентификатор автора комментария.
     */
    long author;

    /**
     * Ссылка на изображение автора комментария.
     */
    String authorImage;

    /**
     * Имя автора комментария.
     */
    String authorFirstName;

    /**
     * Дата создания комментария.
     */
    long createdAt;

    /**
     * Уникальный идентификатор комментария.
     */
    long pk;

    /**
     * Текст комментария.
     */
    String text;

    public CommentDto(long author,
                      String authorImage,
                      String authorFirstName,
                      long pk,
                      String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.pk = pk;
        this.text = text;
    }

}