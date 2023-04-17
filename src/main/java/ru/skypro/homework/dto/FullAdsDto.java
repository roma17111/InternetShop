package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FullAdsDto {

    // id объявления
    int pk;
    String authorFirstName;
    String authorLastName;
    String description;
    String email;
    // url image
    String image;
    String phone;
    int price;
    String title;

}
