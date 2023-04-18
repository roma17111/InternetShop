package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
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

    ResponseWrapperAds ads = new ResponseWrapperAds(1,
            List.of(new AdsDto(1, "/ads/image/test",
                    1, 20000,
                    "Пятая плойка)))")));

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

}
