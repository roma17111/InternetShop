package ru.skypro.homework.models;

import lombok.*;

import javax.persistence.*;

/**
 * Класс модели для аватаров пользователей.
 */
@Data
@Entity
@Table(name = "avatar")
public class Avatar {

    /**
     * ID аватара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Размер файла аватара
     */
    @Column(name = "file_size")
    private Integer fileSize;

    /**
     * Тип изображения аватара
     */
    @Column(name = "image_type")
    private String imageType;

    /**
     * Путь к аватару
     */
    @Column(name = "avatar_path")
    private String avatarPath;
}



