package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.configurations.CustomUserDetailsService;
import ru.skypro.homework.mdels.Ads;
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
    public void updateAd(Ads ads) {
        adsRepository.save(ads);
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
    public Ads findById(long id) {
        return adsRepository.findById(id).orElse(null);
    }
}
