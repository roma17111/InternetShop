package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.AvatarService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AvatarService avatarService;

    @InjectMocks
    private UserController userController;

    @Test
    void testUpdateUser() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setEmail("testuser@test.ru");

        when(authService.updateAuthUser(userInfoDto)).thenReturn(userInfoDto);

        UserInfoDto response = userController.updateUser(userInfoDto);

        assertEquals(userInfoDto, response);
    }

    @Test
    void testGetUser() {
        String userName = "testuser@test.ru";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userName);

        when(authService.getEmailFromAuthUser()).thenReturn(userName);
        when(authService.getByUserName(userName)).thenReturn(userInfo);

        UserInfoDto response = userController.getUser();

        assertEquals(userName, response.getEmail());
    }

    @Test
    void testSetPassword() {
        NewPassword password = new NewPassword();
        password.setNewPassword("testpass");

        doNothing().when(authService).setPasswordFromUser(password.getNewPassword());

        NewPassword response = userController.setPassword(password);

        assertNotNull(response);
    }

    @Test
    void testUpdateUserImage() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test image content".getBytes());

        doNothing().when(authService).updateUserImage(image);

        ResponseEntity<?> response = userController.updateUserImage(image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

 /*   @Test
    void testGetAvatarImageUser() throws ExecutionException, InterruptedException {
        long id = 1L;
        long avatarId = 2L;
        UserInfo userInfo = new UserInfo();
        Avatar avatar = new Avatar();
        avatar.setId(avatarId);
        userInfo.setAvatar(avatar);
        byte[] imageBytes = "test image content".getBytes();

        when(authService.getById(id)).thenReturn(userInfo);
        when(avatarService.getAvatarImage(avatarId)).thenReturn(imageBytes);

        ResponseEntity<byte[]> response = userController.getAvatarImageUser(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageBytes, response.getBody());
    }*/
}