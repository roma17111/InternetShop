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
    @ManyToOne(fetch = FetchType.EAGER)
    UserInfo author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ads_id",referencedColumnName = "ads_id")
    private Ads ads;
    int date = (int) System.currentTimeMillis();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String text;
}
