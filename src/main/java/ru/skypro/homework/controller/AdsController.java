package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.AvatarService;

import javax.servlet.annotation.MultipartConfig;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Контроллер для работы с объявлениями и комментариями.
 */
@Log4j2
@CrossOrigin(value = {"*"})
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;
    private final AvatarService avatarService;
    private final AuthService authService;

    /**
     * Возвращает аватар комментария.
     *
     * @param id идентификатор комментария
     * @return аватар комментария
     * @throws ExecutionException   если возникает ошибка во время выполнения
     * @throws InterruptedException если выполнение прерывается
     */
    @GetMapping("/comments/avatars2/{id}")
    public ResponseEntity<byte[]> getAvatarImageComments(@PathVariable long id) throws ExecutionException, InterruptedException {
        byte[] imageBytes = adsService.getCommentById(id).getAuthor().getImage();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageBytes);
    }

    /**
     * Возвращает аватар объявления.
     *
     * @param id идентификатор объявления
     * @return аватар объявления
     * @throws ExecutionException   если возникает ошибка во время выполнения
     * @throws InterruptedException если выполнение прерывается
     */
    @GetMapping("/avatars2/{id}")
    public ResponseEntity<byte[]> getAvatarImageAds(@PathVariable long id) throws ExecutionException, InterruptedException {
        byte[] imageBytes = adsService.findById(id).getImage();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageBytes);
    }

    /**
     * Возвращает список объявлений авторизованного пользователя.
     *
     * @return список объявлений авторизованного пользователя
     */
    @GetMapping("/me")
    @Operation(summary = "Получить объявления авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseWrapperAds getAdsMe() {
        List<AdsDto> adsDtoList = adsService.getAdsDtoListFromAuthUser();
        return new ResponseWrapperAds(adsDtoList.size(), adsDtoList);
    }

    /**
     * Возвращает список всех объявлений.
     *
     * @return список всех объявлений
     */
    @GetMapping
    @Operation(summary = "Получить все объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseWrapperAds getAllAds() {
        List<AdsDto> adsDtoList = adsService.getAdsFromAllUsers();
        return new ResponseWrapperAds(adsDtoList.size(), adsDtoList);
    }

    /**
     * Создает новое объявление.
     *
     * @param image      изображение для объявления
     * @param properties свойства объявления
     * @return созданное объявление
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавить объявление")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public AdsDto createAdd(@RequestPart(name = "image")
                                       MultipartFile image,
                                       @RequestPart(name = "properties")
                                       CreateAdsDto properties) {
       return adsService.uploadFileAndAd(image, properties);
    }

    /**
     * Возвращает список комментариев для объявления.
     *
     * @param id идентификатор объявления
     * @return список комментариев
     */
    @GetMapping("/{id}/comments")
    @Operation(summary = "Получить комментарии объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseWrapperComment getComments(@PathVariable int id) {
        List<CommentDto> commentDtoList = adsService.getCommentDtoList(id);
        return new ResponseWrapperComment(commentDtoList.size(),
                commentDtoList);
    }

    /**
     * Добавляет комментарий к объявлению.
     *
     * @param id         идентификатор объявления
     * @param commentDto данные комментария
     * @return созданный комментарий
     */
    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавить комментарий к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public CommentDto addComment(@PathVariable int id,
                                 @RequestBody CommentDto commentDto) {
        Comment comment1 = new Comment(commentDto.getText());
        adsService.addCommentToAd(comment1, id);
        return Comment.mapToCommentDto(comment1);
    }

    /**
     * Возвращает информацию об объявлении.
     *
     * @param id идентификатор объявления
     * @return информация об объявлении
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public FullAdsDto getAds(@PathVariable long id) {
        return adsService.getFullAd(id);
    }

    /**
     * Обновляет информацию об объявлении.
     *
     * @param id     идентификатор объявления
     * @param adsDto данные для обновления объявления
     * @return обновленная информация об объявлении
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновить информацию об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public FullAdsDto updateAds(@PathVariable int id,
                                @RequestBody CreateAdsDto adsDto) {
        if (authService.userIsAdmin() || adsService.isUserOwnerToAds(id)) {
            return adsService.updateAdsToAuthUser(id, adsDto);

        }
        ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        throw new UnsupportedOperationException("Not access to operation");
    }

    /**
     * Удаляет объявление.
     *
     * @param id идентификатор объявления
     * @return статус ответа
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Удалить объявление")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        if (authService.userIsAdmin() || adsService.isUserOwnerToAds(id)) {
            Ads ads = adsService.findById(id);
            adsService.deleteAdd(ads);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id    идентификатор объявления
     * @param image новое изображение
     * @return статус ответа
     */
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновить картинку объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<?> updateAdImage(@PathVariable long id,
                                           @RequestPart MultipartFile image) {
        adsService.updateAdImageFromAuthUser(id, image);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет комментарий.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @return обновленный список комментариев
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удалить комментарий")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseWrapperComment deleteComment(@PathVariable int adId,
                                                @PathVariable int commentId) {
        if (authService.userIsAdmin() || adsService.isUserOwnerToComment(commentId)) {
            adsService.deleteComment(adId, commentId);
            List<Comment> comments = adsService.findById(adId).getComments();
            return new ResponseWrapperComment(comments.size(), adsService.mapListToCommentDto(comments));
        }
        ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        throw new UnsupportedOperationException("Not access to operation");
    }

    /**
     * Обновляет комментарий.
     *
     * @param adId       идентификатор объявления
     * @param commentId  идентификатор комментария
     * @param commentDto данные для обновления комментария
     * @return обновленный комментарий
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновить комментарий")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public CommentDto updateComment(@PathVariable int adId,
                                    @PathVariable int commentId,
                                    @RequestBody CommentDto commentDto) {
        if (authService.userIsAdmin() || adsService.isUserOwnerToComment(commentId)) {
            return adsService.updateComment(adId, commentId, commentDto);
        }
        ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        throw new UnsupportedOperationException("Not access to operation");
    }
}
