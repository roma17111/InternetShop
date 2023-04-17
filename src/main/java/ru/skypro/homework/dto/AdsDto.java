package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdsDto {

    // id автора объявления
    long author;

    // ссылка на картинку объявления
    String image;

    // id объявления
    long pk;
    int price;
    String title;

}
