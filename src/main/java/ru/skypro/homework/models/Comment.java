package ru.skypro.homework.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.dto.CommentDto;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Comment_table")
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
    int date = (int) System.currentTimeMillis();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;
    String text;

    public Comment(String text) {
        this.text = text;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getAuthor().getId(),
                "/ads/comments/"+String.valueOf(comment.getId() ),
                comment.getAuthor().getFirstName(),
                comment.getId(),
                comment.getText());
    }
}
