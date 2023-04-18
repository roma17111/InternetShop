package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import ru.skypro.homework.service.AuthService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;


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
        System.out.println("Hello");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        System.out.println(authentication.isAuthenticated());
        UserInfoDto userInfoDto1 = new UserInfoDto(userInfoDto.getFirstName(),
                userInfoDto.getLastName(),
                userInfoDto.getPhone());
        userInfoDto.setImage("pictures/docker-sh.png");
        return userInfoDto1;
    }

    @GetMapping("/me")
    public UserInfoDto getUser() {
        return user;
    }

    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody NewPassword password) {
        return new NewPassword();
    }

    @PatchMapping(value = "/me/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam MultipartFile image) throws IOException {
        user.setImage("/users/image/test");
        System.out.println("Done");
        System.out.println(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/image/test")
    public ResponseEntity<byte[]> getUserImage() {
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

}

