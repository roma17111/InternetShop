package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    // id author
    long author;
    // url image
    String authorImage;
    String authorFirstName;
    long createdAt;
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
