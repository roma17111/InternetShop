package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ResponseWrapperAds;

@Slf4j
@RestController
public class AdsController {

    @GetMapping("/ads")
    private ResponseWrapperAds getAds() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        System.out.println(authentication.isAuthenticated());
        Ads[] ads = {new Ads(1,
                "https://klike.net/uploads/posts/2019-01/1547623484_4.jpg",
                1,
                12345,
                "Заголовок",
                "Описание")};
        ResponseWrapperAds adds = new ResponseWrapperAds(1,ads);
        System.out.println(adds);
        return adds;
    }
}
