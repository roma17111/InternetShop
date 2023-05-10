package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс исключения, которое выбрасывается, когда запрашиваемый ресурс не найден.
 * Соответствует статусу HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Конструктор для создания исключения с сообщением.
     * @param message Сообщение об ошибке.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}