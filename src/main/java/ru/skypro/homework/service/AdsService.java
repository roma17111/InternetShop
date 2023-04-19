package ru.skypro.homework.service;

import ru.skypro.homework.mdels.Ads;
import ru.skypro.homework.mdels.Comment;

import java.util.List;

public interface AdsService {
    void addAd(Ads ads);

    void updateAd(Ads ads);

    List<Ads> getAdsFromAuthUser();

    List<Ads> getAllAds();

    void deleteAdd(Ads ads);

    Ads findById(long id);

    void addCommentToAd(Comment comment,
                        long id);
}
