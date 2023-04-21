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
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.repository.UserRepository;

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
    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseWrapperAds getAdsMe() {
        List<AdsDto> adsDtoList = adsService.getAdsDtoListFromAuthUser();
        return new ResponseWrapperAds(adsDtoList.size(), adsDtoList);
    }

    @GetMapping
    public ResponseWrapperAds getAllAds() {
        List<AdsDto> adsDtoList = adsService.getAdsFromAllUsers();
        return new ResponseWrapperAds(adsDtoList.size(), adsDtoList);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAdd(@RequestPart(name = "image")
                                       MultipartFile image,
                                       @RequestPart(name = "properties")
                                       CreateAdsDto properties) {
        adsService.uploadFileAndAd(image,properties);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        Ads ads = adsService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(ads.getAdsImage());
    }

    @GetMapping("/{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable int id) {
        List<CommentDto> commentDtoList = adsService.getCommentDtoList(id);
        return new ResponseWrapperComment(commentDtoList.size(),
                commentDtoList);
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
        return adsService.getFullAd(id);
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

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    public ResponseWrapperComment deleteComment(@PathVariable int adId,
                                                @PathVariable int commentId) {
        adsService.deleteComment(adId, commentId);
        List<Comment> comments = adsService.findById(adId).getComments();
        return new ResponseWrapperComment(comments.size(), adsService.mapListToCommentDto(comments));
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable int adId,
                                    @PathVariable int commentId,
                                    @RequestBody CommentDto commentDto) {
        return adsService.updateComment(adId, commentId, commentDto);
    }
}
