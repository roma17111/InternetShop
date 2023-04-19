package ru.skypro.homework.service;

import ru.skypro.homework.mdels.Ads;

import java.util.List;

public interface AdsService {
    void addAd(Ads ads);

    void updateAd(Ads ads);

    List<Ads> getAdsFromAuthUser();

    List<Ads> getAllAds();

    Ads findById(long id);
}
