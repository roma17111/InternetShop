package ru.skypro.homework.controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.service.impl.AvatarServiceImpl;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(value = {"http://localhost:3000",
        "http://java-mouse.ru",
        "http://ovz3.j04912775.wmekm.vps.myjino.ru"})
@RequiredArgsConstructor
public class TestControllerAvatar {

   private final AvatarServiceImpl avatarService;

    @PostMapping(value = "/testSave", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> testSave(@RequestParam("file") MultipartFile file) {
        Avatar avatar = new Avatar();
        avatarService.testSave(file, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatars2/{id}{dataId}")
    public ResponseEntity<byte[]> getAvatarImage(@PathVariable long id,
                                                 @PathVariable long dataId) throws ExecutionException, InterruptedException {
        byte[] imageBytes = avatarService.getAvatarImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @GetMapping("/avatars1/{id}")
    public ResponseEntity<Resource> getAvatar(@PathVariable Integer id) throws JSchException, SftpException, IOException, ExecutionException, InterruptedException {
        Resource resource = avatarService.getAvatarResource(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/avatars/{id}/download")
    public ResponseEntity<Resource> downloadAvatar(@PathVariable Integer id) throws JSchException, SftpException, IOException, ExecutionException, InterruptedException {
        Resource resource = avatarService.getAvatarResource(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"avatar.jpg\"")
                .body(resource);
    }
}
