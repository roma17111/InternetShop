package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseWrapperComment {

    //Общее количество комментариев
    int count;

}
