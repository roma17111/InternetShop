package ru.skypro.homework.mdels;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    // id author
    @OneToOne
    @JoinColumn(name = "author", referencedColumnName = "user_id")
    UserInfo author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ads",referencedColumnName = "ads_id")
    private Ads ads;
    int date = new Date().getDate();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String text;
}
