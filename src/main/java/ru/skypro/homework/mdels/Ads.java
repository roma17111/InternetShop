package ru.skypro.homework.mdels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Ads {

    @ManyToOne(fetch = FetchType.EAGER)
    private UserInfo author;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "ads")
    private List<Comment> comments;

    @Column(name = "image_path")
    byte[] adsImage;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    long id;
    int price;
    String title;
    @JsonIgnore
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
                "/ads/image/test",
                ads.getAuthor().getPhone(),
                ads.getPrice(),
                ads.getTitle());
    }

    public static AdsDto mapToAdsDto(Ads ads) {
        return new AdsDto(ads.getAuthor().getId(),
                "/ads/image/test",
                ads.getId(),
                ads.getPrice(),
                ads.getTitle());
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
