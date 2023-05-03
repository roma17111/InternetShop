package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.repository.AdsRepository;
import ru.skypro.homework.service.repository.CommentRepository;
import ru.skypro.homework.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsServiceImpl implements AdsService {

    private final AvatarService avatarService;
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
        adsRepository.save(ads);
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
        log.info("added comment " + comment.getText() + "to ads " + ads.getDescription());
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

        // если юзер-админ, То во фронтенде будет покажываться ручка и крестик во всех коммент
        if (authService.userIsAdmin()) {
            String email = authService.getEmailFromAuthUser();
            UserInfo userInfo = userRepository.findByEmail(email);
            for (CommentDto dto : commentDtoList) {
                // По логике фронтенда карандаш и крестик рисуется если твой id из бвзы
                // совпадает с pk в dto. Нам ничего не мешает подменить фронтенду pk
                // на нужный, что бы он брался не из базы автора, а от админа
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

    @SneakyThrows
    @Override
    public AdsDto uploadFileAndAd(MultipartFile image,
                                  CreateAdsDto properties) {
        Ads ads1 = new Ads(properties.getPrice(),
                properties.getTitle(),
                properties.getDescription());
        byte[] i = image.getBytes();
        ads1.setImage(i);
        addAd(ads1);
        log.info("added ads - " + ads1);
        Avatar avatar = avatarService.testSave(image, MediaType.parseMediaType(Objects.requireNonNull(image.getContentType())));
        ads1.setAvatar(avatar);
        updateAd(ads1);
        return Ads.mapToAdsDto(ads1);
    }

    @Override
    public FullAdsDto getFullAd(long id) {
        Ads ads1 = findById(id);
        // если юзер-админ, То во фронтенде будет покажываться ручка и крестик во всех объявлениях
        if (authService.userIsAdmin()) {
            String email = authService.getEmailFromAuthUser();
            UserInfo userInfo = userRepository.findByEmail(email);
            FullAdsDto fullAdsDto = Ads.mapToFullAdDto(ads1);
            // По логике фронтенда карандаш и крестик рисуется если твой email
            // совпадает с email в dto. Нам ничего не мешает подменить фронтенду email
            // на нужный, что бы он брался не из базы автора а от админа
            fullAdsDto.setEmail(userInfo.getEmail());
            return fullAdsDto;
        }
        return Ads.mapToFullAdDto(ads1);
    }

    @SneakyThrows
    @Override
    public void updateAdImageFromAuthUser(long id,
                                          MultipartFile image) {
        Ads ads = findById(id);
        Avatar avatar = avatarService.testSave(image, MediaType.parseMediaType(Objects.requireNonNull(image.getContentType())));
        ads.setAvatar(avatar);
        byte[] i = image.getBytes();
        ads.setImage(i);
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

    @Override
    public boolean isUserOwnerToAds(long id) {
        Ads ads = findById(id);
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        return ads.getAuthor().equals(userInfo);
    }

    @Override
    public boolean isUserOwnerToComment(long id) {
        Comment comment = getCommentById(id);
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        return comment.getAuthor().equals(userInfo);
    }

}
