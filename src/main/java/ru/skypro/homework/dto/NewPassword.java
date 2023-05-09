package ru.skypro.homework.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewPassword {

    @NotBlank
    String currentPassword;

    @NotBlank
    String newPassword;
}
