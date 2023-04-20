package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    // id author
    long author;
    // url image
    String authorImage = "/users/"+String.valueOf(this.author)+"/image  ";
    String authorFirstName;
    int createdAt = (int) System.currentTimeMillis();
    long pk;
    String text;

    public CommentDto(long author,
                      String authorImage,
                      String authorFirstName,
                      long pk,
                      String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.pk = pk;
        this.text = text;
    }

}
