package ru.skypro.homework.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewPassword {
    String currentPassword;
    String newPassword;
}
