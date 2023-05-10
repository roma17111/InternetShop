package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Класс, представляющий обертку для ответа, содержащего список комментариев (CommentDto).
 * Используется для передачи данных между слоями приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseWrapperComment {

    /**
     * Общее количество комментариев в ответе.
     */
    int count;

    /**
     * Список комментариев.
     */
    List<CommentDto> results;

    /**
     * Метод добавления комментария в список комментариев.
     *
     * @param comment Комментарий в формате CommentDto.
     */
    public void addComment(CommentDto comment) {
        results.add(comment);
    }
}
