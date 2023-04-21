package ru.skypro.homework.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.FullAdsDto;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"comments"})
@Table(name = "ads_table")
public class Ads {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    UserInfo author;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "ads")
    List<Comment> comments;

    @Column(name = "image_path")
    byte[] adsImage;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    long id;
    int price;
    String title;
    String description;

    public Ads(int price, String title, String description) {
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public static FullAdsDto mapToFullAdDto(Ads ads) {
        return new FullAdsDto(ads.getId(),
                ads.getAuthor().getFirstName(),
                ads.getAuthor().getLastName(),
                ads.getDescription(),
                ads.getAuthor().getEmail(),
                "/ads/" + String.valueOf(ads.getId()) + "/image",
                ads.getAuthor().getPhone(),
                ads.getPrice(),
                ads.getTitle());
    }

    public static AdsDto mapToAdsDto(Ads ads) {
        return new AdsDto(ads.getAuthor().getId(),
                "/ads/" + String.valueOf(ads.getId()) + "/image",
                ads.getId(),
                ads.getPrice(),
                ads.getTitle());
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }
}
