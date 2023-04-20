package ru.skypro.homework.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.mdels.Ads;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = false)
public class CreateAdsDto {
    String description;
    int price;
    String title;

}
