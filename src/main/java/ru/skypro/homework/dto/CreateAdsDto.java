package ru.skypro.homework.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Класс, представляющий DTO для создания объявлений.
 *
 * <p>DTO (Data Transfer Object) используется для передачи данных между слоями
 * приложения, и не содержит бизнес-логики. Класс CreateAdsDto содержит поля,
 * которые необходимы для создания объявления, и генерирует необходимые методы
 * автоматически при помощи библиотеки Lombok.</p>
 *
 * @author
 * @version
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = false)
public class CreateAdsDto {
    /**
     * Описание объявления.
     */
    String description;

    /**
     * Цена объявления.
     */
    int price;

    /**
     * Заголовок объявления.
     */
    String title;
}
