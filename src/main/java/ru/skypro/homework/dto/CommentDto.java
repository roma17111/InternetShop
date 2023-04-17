package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    // id author
    int author;

    // url image
    String authorImage;
    String authorFirstName;
    int date = new Date().getDate();
    int pk;
    String text;
}
