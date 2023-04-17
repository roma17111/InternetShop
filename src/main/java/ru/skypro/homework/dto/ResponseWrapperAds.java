package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseWrapperAds {

    int count;
    AdsDto[] result;

    public ResponseWrapperAds() {

    }
}
