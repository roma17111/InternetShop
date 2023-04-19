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
        userInfo.addAdFromUser(ads);
        userRepository.save(userInfo);

    }
}
