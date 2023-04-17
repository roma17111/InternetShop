package ru.skypro.homework.mdels;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    // id author
    @OneToOne
    @JoinColumn(name = "id",referencedColumnName = "user_id")
    UserInfo userInfo;

    // url image
    String authorImage;
    String authorFirstName;
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String text;
}
