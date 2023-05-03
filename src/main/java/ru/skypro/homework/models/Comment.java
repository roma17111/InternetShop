package ru.skypro.homework.models;

import jdk.jfr.Timespan;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.skypro.homework.dto.CommentDto;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "test_ccoment")
public class Comment {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    UserInfo author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "ads_id", referencedColumnName = "ads_id")
    Ads ads;
    long date = (new Date().getTime()/1000);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;
    String text;

    public Comment(String text) {
        this.text = text;
    }

    private String validCommentUrlImageUser(Comment comment) {
            return "/ads/comments/avatars2/" + String.valueOf(comment.getId());
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getAuthor().getId(),
                comment.validCommentUrlImageUser(comment),
                comment.getAuthor().getFirstName(),
                comment.getDate(),
                comment.getId(),
                comment.getText());
    }
}
