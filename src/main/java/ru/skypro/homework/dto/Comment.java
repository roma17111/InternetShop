package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    // id author
    int author;

    // url image
    String authorImage;
    String authorFirstName;
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
    int pk;
    String text;
}
