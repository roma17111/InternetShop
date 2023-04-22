package ru.skypro.homework.controller;

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

import java.io.*;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PatchMapping("/me")
    public UserInfoDto updateUser(@RequestBody UserInfoDto userInfoDto) {
        return authService.updateAuthUser(userInfoDto);
    }

    @GetMapping("/me")
    public UserInfoDto getUser() {
        String userName = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(userName);
        return UserInfo.mapToUserInfoDto(userInfo);
    }

    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody NewPassword password) {
        authService.setPasswordFromUser(password.getNewPassword());
        return new NewPassword();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam MultipartFile image) throws IOException {
        authService.updateUserImage(image);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable long id) {
        UserInfo userInfo = authService.getById(id);
        System.out.println(userInfo);
        byte[] userImg = userInfo.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userImg);
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImageFromAuthUser() {
        String email = authService.getEmailFromAuthUser();
        UserInfo userInfo = authService.getByUserName(email);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userInfo.getImage());
    }

}

