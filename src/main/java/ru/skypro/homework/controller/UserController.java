package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.AvatarService;

import java.io.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@CrossOrigin(value = {"http://localhost:3000",
        "http://java-mouse.ru",
        "http://ovz3.j04912775.wmekm.vps.myjino.ru"})
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final AvatarService avatarService;

    /**
     * Обновляет информацию об авторизованном пользователе
     *
     * @param userInfoDto информация о пользователе
     * @return обновленная информация о пользователе
     */
    @PatchMapping("/me")
    @Operation(summary = "Обновить информацию об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "201", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public UserInfoDto updateUser(@RequestBody UserInfoDto userInfoDto) {
        return authService.updateAuthUser(userInfoDto);
    }

    /**
     * Возвращает информацию об авторизованном пользователе
     *
     * @return информация о пользователе
     */
    @GetMapping("/me")
    @Operation(summary = "Получить информацию об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public UserInfoDto getUser() {
        String userName = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(userName);
        return UserInfo.mapToUserInfoDto(userInfo);
    }

    /**
     * Обновляет пароль авторизованного пользователя
     *
     * @param password новый пароль
     * @return объект с новым паролем
     */
    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public NewPassword setPassword(@RequestBody NewPassword password) {
        authService.setPasswordFromUser(password.getNewPassword());
        return new NewPassword();
    }

    /**
     * Обновляет аватар авторизованного пользователя
     *
     * @param image файл изображения аватара
     * @return HTTP-статус
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновить аватар авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<?> updateUserImage(@RequestParam MultipartFile image) {
        authService.updateUserImage(image);
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает аватар пользователя в виде массива байтов
     *
     * @param id идентификатор пользователя
     * @return массив байтов изображения аватара
     * @throws ExecutionException   ошибка выполнения
     * @throws InterruptedException ошибка прерывания потока
     */
    @GetMapping("/avatars2/{id}")
    @Operation(summary = "Показать аватар пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<byte[]> getAvatarImageUser(@PathVariable long id) throws ExecutionException, InterruptedException {
        byte[] imageBytes = authService.getById(id).getImage();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageBytes);
    }
}
