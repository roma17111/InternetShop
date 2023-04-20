package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.configurations.CustomUserDetailsService;
import ru.skypro.homework.mdels.Ads;
import ru.skypro.homework.mdels.Comment;
import ru.skypro.homework.mdels.UserInfo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.repository.AdsRepository;
import ru.skypro.homework.service.repository.CommentRepository;
import ru.skypro.homework.service.repository.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;

    @Override
    public void addAd(Ads ads) {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        System.out.println(userInfo);
        userInfo.addAdFromUser(ads);
        ads.setAuthor(userInfo);
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

    @Override
    public List<Ads> getAllAds() {
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
        adsRepository.save(ads);
    }
}
