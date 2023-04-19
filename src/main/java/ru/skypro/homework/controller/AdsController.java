package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mdels.Ads;
import ru.skypro.homework.service.AdsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;

    //Потом уберём временные коллекции и лишние объекты - временная мера для заглушек
    List<CommentDto> c = new ArrayList<>();

    {
        c.add(new CommentDto(1, "/users/image/test",
                "Roman", 1, "Отличный продавец!" +
                " Рекомендую!!!"));
    }

    List<AdsDto> a = new ArrayList<>();

    {
        a.add(new AdsDto(1, "/ads/image/test",
                1, 20000,
                "Пятая плойка)))"));

    }

    private ResponseWrapperAds ads = new ResponseWrapperAds(1, a);

    private ResponseWrapperComment comment = new ResponseWrapperComment(1, c);

    FullAdsDto fullAds = new FullAdsDto(1,
            "Roman",
            "Tupego",
            "Продаю пятую плойку, по причине отсутствия",
            "user@gmail.com"
            , "/users/image/test", "+78000000000",
            20000,
            "Пятая плойка");

    @GetMapping("/me")
    public ResponseWrapperAds getAdsMe() {
        List<Ads> adsList = adsService.getAdsFromAuthUser();
        List<AdsDto> adsDtoList = new ArrayList<>();
        for (int i = 0; i < adsList.size(); i++) {
            adsDtoList.add(Ads.mapToAdsDto(adsList.get(i)));
        }
        return new ResponseWrapperAds(adsDtoList.size(), adsDtoList);
    }

    @GetMapping
    public ResponseWrapperAds getAllAds() {
        List<Ads> adsList = adsService.getAllAds();
        List<AdsDto> adsDtoList = new ArrayList<>();
        for (int i = 0; i < adsList.size(); i++) {
            adsDtoList.add(Ads.mapToAdsDto(adsList.get(i)));
        }
        return new ResponseWrapperAds(adsDtoList.size(), adsDtoList);
    }


    //Теперь всё ок)))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAdd(@RequestPart(name = "image")
                                       MultipartFile image,
                                       @RequestPart(name = "properties")
                                       CreateAdsDto properties) {
        Ads ads1 = new Ads(properties.getPrice(),
                properties.getTitle(),
                properties.getDescription());
        System.out.println(ads1);
        adsService.addAd(ads1);
        log.info("added ads - " + ads1);
        return ResponseEntity.ok().build();
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

    @PostMapping("/{id}/comments")
    public CommentDto addComment(@PathVariable int id,
                                 @RequestBody CommentDto commentDto) {
        CommentDto commentDto1 = new CommentDto(1,
                "/users/image/test",
                "Roman", 1, commentDto.getText()
        );
        c.add(commentDto1);
        return commentDto1;
    }

    @GetMapping("/{id}")
    public FullAdsDto getAds(@PathVariable long id) {
        Ads ads1 = adsService.findById(id);
        return Ads.mapToFullAdDto(ads1);
    }

    @PatchMapping("/{id}")
    public FullAdsDto updateAds(@PathVariable int id,
                                @RequestBody CreateAdsDto adsDto) {
        Ads ads1 = adsService.findById(id);
        ads1.setDescription(ads1.getDescription());
        ads1.setPrice(adsDto.getPrice());
        ads1.setTitle(ads1.getTitle());
        adsService.updateAd(ads1);
        return Ads.mapToFullAdDto(ads1);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        ads = null;
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int adId,
                                           @PathVariable int commentId) {
        comment = null;
        return ResponseEntity.ok().build();
    }
}
