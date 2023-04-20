package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAdd(@RequestPart(name = "image")
                                       MultipartFile image,
                                       @RequestPart(name = "properties")
                                       CreateAdsDto properties) {
        Ads ads1 = new Ads(properties.getPrice(),
                properties.getTitle(),
                properties.getDescription());
        try {
            ads1.setAdsImage(image.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        adsService.addAd(ads1);
        log.info("added ads - " + ads1);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        Ads ads = adsService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(ads.getAdsImage());
    }

    @GetMapping("/{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable int id) {
        Ads ads = adsService.findById(id);
        List<Comment> comments = ads.getComments();
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment1 : comments) {
            commentDtoList.add(Comment.mapToCommentDto(comment1));
        }
        return new ResponseWrapperComment(commentDtoList.size(), commentDtoList);
    }

    @PostMapping("/{id}/comments")
    public CommentDto addComment(@PathVariable int id,
                                 @RequestBody CommentDto commentDto) {
        Comment comment1 = new Comment(commentDto.getText());
        adsService.addCommentToAd(comment1, id);
        return Comment.mapToCommentDto(comment1);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<byte[]> getImageComments(@PathVariable long id) {
        Comment comment = adsService.getCommentById(id);
        byte[] image = comment.getAuthor().getImage();
        return ResponseEntity.ok().body(image);
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
        Ads ads = adsService.findById(id);
        adsService.deleteAdd(ads);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAd(@PathVariable long id,
                                      @RequestPart MultipartFile image) {
        Ads ads = adsService.findById(id);
        try {
            ads.setAdsImage(image.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        adsService.updateAd(ads);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int adId,
                                           @PathVariable int commentId) {
        adsService.deleteComment(adId,commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable int adId,
                                           @PathVariable int commentId,
                                           @RequestBody CommentDto commentDto) {
        adsService.updateComment(adId,commentId,commentDto);
        return commentDto;
    }
}
