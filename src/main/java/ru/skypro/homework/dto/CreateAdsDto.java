package ru.skypro.homework.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.mdels.Ads;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CreateAdsDto {
    String description;
    int price;
    String title;

}
