package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.repository.AdsRepository;
import ru.skypro.homework.service.repository.CommentRepository;
import ru.skypro.homework.service.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public void addAd(Ads ads) {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        userInfo.addAdFromUser(ads);
        ads.setAuthor(userInfo);
        log.info("added add " + ads.getDescription());
        userRepository.save(userInfo);
    }

    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void updateAd(Ads ads) {
        adsRepository.save(ads);
        log.info("updated ad " + ads);
    }

    @Override
    public List<Ads> getAdsFromAuthUser() {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        return userInfo.getAds();
    }

    private List<Ads> getAllAds() {
        return adsRepository.findAll();
    }

    @Override
    public void deleteAdd(Ads ads) {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        userInfo.deleteAdFromUser(ads);
        for (Comment comment : ads.getComments()) {
            userInfo.deleteComment(comment);
        }
        userRepository.save(userInfo);
        commentRepository.deleteAll(ads.getComments());
        log.warn("ads deleted!!! " + ads);
        adsRepository.delete(ads);
    }

    @Override
    public Ads findById(long id) {
        return adsRepository.findById(id).orElse(null);
    }

    @Override
    public void addCommentToAd(Comment comment,
                               long id) {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        Ads ads = findById(id);
        comment.setAuthor(userInfo);
        comment.setAds(ads);
        ads.addComment(comment);
        log.info("added comment " + comment.getText()+"to ads "+ ads.getDescription());
        commentRepository.save(comment);
        adsRepository.save(ads);
    }

    @Override
    public void deleteComment(long adId, long commentId) {
        Ads ads = findById(adId);
        UserInfo userInfo = ads.getAuthor();
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(NullPointerException::new);
        ads.deleteComment(comment);
        userInfo.deleteComment(comment);
        log.warn("comment deleted!" + comment.getText());
        userRepository.save(userInfo);
        adsRepository.save(ads);
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(long adId,
                                    long commentId,
                                    CommentDto commentDto) {
        Ads ads = findById(adId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NullPointerException::new);
        for (Comment adsComment : ads.getComments()) {
            if (adsComment.equals(comment)) {
                adsComment.setText(commentDto.getText());
            }
        }
        comment.setText(commentDto.getText());
        log.info("comment updated!" + comment.getText());
        commentRepository.save(comment);
        updateAd(ads);
        return Comment.mapToCommentDto(comment);
    }

    @Override
    public List<CommentDto> mapListToCommentDto(List<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(Comment.mapToCommentDto(comment));
        }
        return commentDtoList;
    }

    @Override
    public List<CommentDto> getCommentDtoList(long id) {
        Ads ads = findById(id);
        List<Comment> comments = ads.getComments();
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment1 : comments) {
            commentDtoList.add(Comment.mapToCommentDto(comment1));
        }
        if (authService.userIsAdmin()) {
            String email = authService.getEmailFromAuthUser();
            UserInfo userInfo = userRepository.findByEmail(email);
            for (CommentDto dto : commentDtoList) {
                dto.setAuthor(userInfo.getId());
            }
        }
        return commentDtoList;
    }

    @Override
    public List<AdsDto> getAdsDtoListFromAuthUser() {
        List<Ads> adsList = getAdsFromAuthUser();
        List<AdsDto> adsDtoList = new ArrayList<>();
        for (int i = 0; i < adsList.size(); i++) {
            adsDtoList.add(Ads.mapToAdsDto(adsList.get(i)));
        }
        return adsDtoList;
    }

    @Override
    public List<AdsDto> getAdsFromAllUsers() {
        List<Ads> adsList = getAllAds();
        List<AdsDto> adsDtoList = new ArrayList<>();
        for (int i = 0; i < adsList.size(); i++) {
            adsDtoList.add(Ads.mapToAdsDto(adsList.get(i)));
        }
        return adsDtoList;
    }

    @Override
    public void uploadFileAndAd(MultipartFile image,
                                CreateAdsDto properties) {
        Ads ads1 = new Ads(properties.getPrice(),
                properties.getTitle(),
                properties.getDescription());
        try {
            ads1.setAdsImage(image.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        addAd(ads1);
        log.info("added ads - " + ads1);
    }

    @Override
    public FullAdsDto getFullAd(long id) {
        Ads ads1 = findById(id);
        if (authService.userIsAdmin()) {
            String email = authService.getEmailFromAuthUser();
            UserInfo userInfo = userRepository.findByEmail(email);
            FullAdsDto fullAdsDto = Ads.mapToFullAdDto(ads1);
            fullAdsDto.setEmail(userInfo.getEmail());
            return fullAdsDto;
        }
        return Ads.mapToFullAdDto(ads1);
    }

    @Override
    public void updateAdImageFromAuthUser(long id,
                                          MultipartFile image) {
        Ads ads = findById(id);
        try {
            ads.setAdsImage(image.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        updateAd(ads);
    }

    @Override
    public FullAdsDto updateAdsToAuthUser(long id,
                                          CreateAdsDto adsDto) {
        Ads ads1 = findById(id);
        ads1.setDescription(adsDto.getDescription());
        ads1.setPrice(adsDto.getPrice());
        ads1.setTitle(adsDto.getTitle());
        updateAd(ads1);
        return Ads.mapToFullAdDto(ads1);
    }

}
