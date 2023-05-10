package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Класс, представляющий обертку для ответа, содержащего список объявлений (AdsDto).
 * Используется для передачи данных между слоями приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseWrapperAds {

    /**
     * Количество объявлений в ответе.
     */
    int count;

    /**
     * Список объявлений.
     */
    List<AdsDto> results;

    /**
     * Метод добавления объявления в список объявлений.
     *
     * @param ad Объявление в формате AdsDto.
     */
    public void addAdd(AdsDto ad) {
        results.add(ad);
    }
}
