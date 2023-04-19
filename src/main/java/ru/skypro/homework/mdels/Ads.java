package ru.skypro.homework.mdels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.dto.FullAdsDto;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Ads {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "author", referencedColumnName = "user_id")
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
    int id;
    int price;
    String title;
    @JsonIgnore
    String description;

    public static FullAdsDto mapToDto(Ads ads) {
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
}
