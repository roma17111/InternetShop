package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

   private ResponseWrapperAds ads = new ResponseWrapperAds(1,
            List.of(new AdsDto(1, "/ads/image/test",
                    1, 20000,
                    "Пятая плойка)))")));

    private ResponseWrapperComment comment = new ResponseWrapperComment(1,
            List.of(new CommentDto(1,"/users/image/test",
                    "Roman",1,"Отличный продавец!" +
                    " Рекомендую!!!")));

    FullAdsDto fullAds = new FullAdsDto(1,
            "Roman",
            "Tupego",
            "Продаю пятую плойку, по причине отсутствия",
            "user@gmail.com"
    ,"/users/image/test","+78000000000",
            20000,
            "Пятая плойка");

    @GetMapping("/me")
    public ResponseWrapperAds getAdsMe() {
        return ads;
    }

    @GetMapping
    public ResponseWrapperAds getAllAds() {
        return ads;
    }


    @GetMapping(value = "/image/test")
    public ResponseEntity<byte[]> getImage() {
        File file = new File("pictures/47411489_1555472527.344_4yzusU_n.jpg");
        byte[] image;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            try {
                image = inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable int id) {

        return comment;
    }

    @GetMapping("/{id}")
    public FullAdsDto getAds(@PathVariable int id) {
        return fullAds;
    }

    @PatchMapping ("/{id}")
    public FullAdsDto updateAds(@PathVariable int id,
                             @RequestBody CreateAdsDto adsDto) {
        fullAds.setPrice(adsDto.getPrice());
        fullAds.setDescription(adsDto.getDescription());
        fullAds.setDescription(adsDto.getDescription());
        return fullAds;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        ads = null;
        return ResponseEntity.ok().build();
    }

}
