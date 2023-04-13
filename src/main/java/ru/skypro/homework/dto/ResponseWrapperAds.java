package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseWrapperAds {

    int count;
    List<Ads> results;

}
