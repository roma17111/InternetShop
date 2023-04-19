package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class FullAdsDto {

    // id объявления
    long pk;
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
