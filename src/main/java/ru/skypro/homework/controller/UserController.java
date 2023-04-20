package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AuthService;

import java.io.*;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserDetailsManager manager;
    private final AuthService authService;

    UserInfoDto user = new UserInfoDto(
            "user@gmail.com",
            "Ivan",
            "Ivanov",
            "12345");

    @PatchMapping("/me")
    public UserInfoDto updateUser(@RequestBody UserInfoDto userInfoDto) {
        String userName = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(userName);
        userInfo.setFirstName(userInfoDto.getFirstName());
        userInfo.setLastName(userInfoDto.getLastName());
        userInfo.setPhone(userInfoDto.getPhone());
        authService.saveUser(userInfo);
        UserInfoDto userInfoDto1 = UserInfo.mapToUserInfoDto(userInfo);
        return userInfoDto1;
    }

    // мапинг сущности в контроллере
    @GetMapping("/me")
    public UserInfoDto getUser() {
        String userName = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(userName);
        UserInfoDto userInfoDto = UserInfo.mapToUserInfoDto(userInfo);
        return userInfoDto;
    }

    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody NewPassword password) {
        authService.setPasswordFromUser(password.getNewPassword());
        return new NewPassword();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam MultipartFile image) throws IOException {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(email);
        userInfo.setImage(image.getBytes());
        authService.saveUser(userInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/image/test")
    public ResponseEntity<byte[]> getUserImage() {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(email);
        if (userInfo.getImage() == null) {
            File file = new File("pictures/image053-55.jpg");
            byte[] img;
            try {
                FileInputStream inputStream = new FileInputStream(file);
                try {
                    img = inputStream.readAllBytes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
        }

        byte[] userImg = userInfo.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userImg);
    }

}

