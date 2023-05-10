package ru.skypro.homework.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.FullAdsDto;

import javax.persistence.*;
import java.util.List;

/**
 * Класс модели для рекламных объявлений.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"comments","image"})
@Table(name = "test_ads")
public class Ads {

    // Пользователь, автор объявления
    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    UserInfo author;

    // Список комментариев к объявлению
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "ads")
    List<Comment> comments;

    // Аватар пользователя
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    // ID объявления
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    long id;

    // Цена, указанная в объявлении
    int price;

    // Заголовок объявления
    String title;

    // Описание объявления
    String description;

    // Изображение объявления
    @Column(name = "page")
    byte[] image;

    // Конструктор для создания объявления
    public Ads(int price, String title, String description) {
        this.price = price;
        this.title = title;
        this.description = description;
    }

    // Вспомогательный метод для получения URL аватара
    private String getValidAvatar(Ads ads) {
        return  "/ads/avatars2/" + String.valueOf(ads.getId());
    }

    // Методы для преобразования объявления в DTO
    public static FullAdsDto mapToFullAdDto(Ads ads) {
        return new FullAdsDto(ads.getId(),
                ads.getAuthor().getFirstName(),
                ads.getAuthor().getLastName(),
                ads.getDescription(),
                ads.getAuthor().getEmail(),
                ads.getValidAvatar(ads),
                ads.getAuthor().getPhone(),
                ads.getPrice(),
                ads.getTitle());
    }

    public static AdsDto mapToAdsDto(Ads ads) {
        return new AdsDto(ads.getAuthor().getId(),
                ads.getValidAvatar(ads),
                ads.getId(),
                ads.getPrice(),
                ads.getTitle());
    }

    // Методы для добавления и удаления комментариев
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }
}