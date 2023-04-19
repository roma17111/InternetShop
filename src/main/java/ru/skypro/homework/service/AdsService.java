package ru.skypro.homework.service;

import ru.skypro.homework.mdels.Ads;

import java.util.List;

public interface AdsService {
    void addAd(Ads ads);

    List<Ads> getAdsFromAuthUser();

    List<Ads> getAllAds();
}
