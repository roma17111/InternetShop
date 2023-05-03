package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AvatarService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdsControllerTest {

    @Mock
    private AdsService adsService;

    @Mock
    private AvatarService avatarService;

    @InjectMocks
    private AdsController adsController;

    @Test
    public void testGetAvatarImageComments() throws ExecutionException, InterruptedException {
        long id = 1L;
        Comment comment = new Comment();
        UserInfo author = new UserInfo();
        Avatar avatar = new Avatar();
        avatar.setId(2L);
        author.setAvatar(avatar);
        comment.setAuthor(author);
        byte[] imageBytes = new byte[]{1, 2, 3};

        when(adsService.getCommentById(id)).thenReturn(comment);
        when(avatarService.getAvatarImage(avatar.getId())).thenReturn(imageBytes);

        ResponseEntity<byte[]> response = adsController.getAvatarImageComments(id);

        verify(adsService).getCommentById(id);
        verify(avatarService).getAvatarImage(avatar.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageBytes, response.getBody());
    }

    @Test
    public void testGetAdsMe() {
        List<AdsDto> adsDtoList = new ArrayList<>();
        when(adsService.getAdsDtoListFromAuthUser()).thenReturn(adsDtoList);

        ResponseWrapperAds response = adsController.getAdsMe();

        verify(adsService).getAdsDtoListFromAuthUser();
        assertEquals(adsDtoList.size(), response.getCount());
        assertEquals(adsDtoList, response.getResults());
    }

    @Test
    public void testGetAllAds() {
        List<AdsDto> adsDtoList = new ArrayList<>();
        when(adsService.getAdsFromAllUsers()).thenReturn(adsDtoList);

        ResponseWrapperAds response = adsController.getAllAds();

        verify(adsService).getAdsFromAllUsers();
        assertEquals(adsDtoList.size(), response.getCount());
        assertEquals(adsDtoList, response.getResults());
    }

}