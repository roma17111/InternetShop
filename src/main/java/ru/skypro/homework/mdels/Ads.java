package ru.skypro.homework.mdels;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    byte[] image;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    long id;
    int price;
    String title;
}
