package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AvatarService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@CrossOrigin(value = {"http://localhost:3000",
        "http://java-mouse.ru"})
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;
    private final AvatarService avatarService;

    @GetMapping("/comments/avatars2/{id}")
    public ResponseEntity<byte[]> getAvatarImageComments(@PathVariable long id) throws ExecutionException, InterruptedException {
        long a = adsService.getCommentById(id).getAuthor().getAvatar().getId();
        byte[] imageBytes = avatarService.getAvatarImage(a);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @GetMapping("/avatars2/{id}")
    public ResponseEntity<byte[]> getAvatarImageAds(@PathVariable long id) throws ExecutionException, InterruptedException {
        long a = adsService.findById(id).getAvatar().getId();
        byte[] imageBytes = avatarService.getAvatarImage(a);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

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

    @GetMapping("/{id}")
    public FullAdsDto getAds(@PathVariable long id) {
        return adsService.getFullAd(id);
    }

    @PatchMapping("/{id}")
    public FullAdsDto updateAds(@PathVariable int id,
                                @RequestBody CreateAdsDto adsDto) {
        return adsService.updateAdsToAuthUser(id,adsDto);
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
       adsService.updateAdImageFromAuthUser(id,image);
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
