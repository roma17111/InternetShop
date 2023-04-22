package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;

import java.util.List;

public interface AdsService {
    void addAd(Ads ads);

    Comment getCommentById(long id);

    void updateAd(Ads ads);

    List<Ads> getAdsFromAuthUser();

    void deleteAdd(Ads ads);

    Ads findById(long id);

    void addCommentToAd(Comment comment,
                        long id);

    void deleteComment(long adId, long commentId);

    CommentDto updateComment(long adId,
                             long commentId,
                             CommentDto commentDto);


    List<CommentDto> mapListToCommentDto(List<Comment> comments);

    List<CommentDto> getCommentDtoList(long id);

    List<AdsDto> getAdsDtoListFromAuthUser();

    List<AdsDto> getAdsFromAllUsers();

    void uploadFileAndAd(MultipartFile image,
                         CreateAdsDto properties);

    FullAdsDto getFullAd(long id);

    void updateAdImageFromAuthUser(long id,
                                   MultipartFile image);

    FullAdsDto updateAdsToAuthUser(long id,
                                   CreateAdsDto adsDto);
}
