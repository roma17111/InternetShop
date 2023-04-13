package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    int id;
    String email;
    String firstName;
    String lastName;
    String phone;
    // url image
    String image;
}
