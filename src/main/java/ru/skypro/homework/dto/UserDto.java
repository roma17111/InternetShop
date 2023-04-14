package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String email;
    String firstName;
    String lastName;
    String phone;
    String image;

    public UserDto(String email, String firstName, String lastName, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
