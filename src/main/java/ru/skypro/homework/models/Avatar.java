package ru.skypro.homework.models;

import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Table(name = "avatar")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "avatar_path")
    private String avatarPath;
}
