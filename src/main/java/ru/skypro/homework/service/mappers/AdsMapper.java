package ru.skypro.homework.service.mappers;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.mdels.Ads;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    Ads mapAdsDtoToAds(AdsDto adsDto);
    AdsDto mapAdsToAdsDto(Ads ads);
}
