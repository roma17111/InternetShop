package ru.skypro.homework.service;

import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;

import java.util.List;

public interface AdsService {
    void addAd(Ads ads);

    Comment getCommentById(long id);

    void updateAd(Ads ads);

    List<Ads> getAdsFromAuthUser();

    List<Ads> getAllAds();

    void deleteAdd(Ads ads);

    Ads findById(long id);

    void addCommentToAd(Comment comment,
                        long id);

    void deleteComment(long adId, long commentId);
}
