package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ads {

    // id автора объявления
    int author;
    // ссылка на картинку объявления
    String image;
    // id объявления
    int pk;
    int price;
    String title;

}
