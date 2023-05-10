package ru.skypro.homework.models;

import jdk.jfr.Timespan;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.skypro.homework.dto.CommentDto;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Класс модели для комментариев.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "test_ccoment")
public class Comment {

    /**
     * Автор комментария.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    UserInfo author;

    /**
     * Объявление, к которому привязан комментарий.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "ads_id", referencedColumnName = "ads_id")
    Ads ads;

    /**
     * Дата создания комментария.
     */
    long date = (LocalDateTime.now().toInstant(ZoneOffset.ofHours(3)).toEpochMilli());

    /**
     * ID комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    /**
     * Текст комментария.
     */
    String text;

    public Comment(String text) {
        this.text = text;
    }

    /**
     * Вспомогательный метод для формирования URL изображения пользователя, оставившего комментарий.
     * @param comment Объект комментария.
     * @return Строка URL.
     */
    private String validCommentUrlImageUser(Comment comment) {
            return "/ads/comments/avatars2/" + String.valueOf(comment.getId());
    }

    /**
     * Метод для преобразования объекта Comment в объект CommentDto.
     * @param comment Объект комментария.
     * @return Объект CommentDto.
     */
    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getAuthor().getId(),
                comment.validCommentUrlImageUser(comment),
                comment.getAuthor().getFirstName(),
                comment.getDate(),
                comment.getId(),
                comment.getText());
    }
}
