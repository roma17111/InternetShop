package ru.skypro.homework.mdels;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Ads {

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    UserInfo author;

    @Column(name = "image_path")
    byte[] image;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    long pk;
    int price;
    String title;
}
