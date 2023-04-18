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
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;

import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    ResponseWrapperAds ads = new ResponseWrapperAds(1,
            List.of(new AdsDto(1,"pictures/docker-sh.png",
                    1,20000,
                    "Плэйстэйшн пять")));

    @GetMapping("/me")
    public ResponseWrapperAds getAdsMe() {
        return ads;
    }

    @GetMapping
    public ResponseWrapperAds getAllAds() {
        return ads;
    }

}
