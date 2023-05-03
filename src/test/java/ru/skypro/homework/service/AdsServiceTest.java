package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.impl.AdsServiceImpl;
import ru.skypro.homework.service.repository.AdsRepository;
import ru.skypro.homework.service.repository.CommentRepository;
import ru.skypro.homework.service.repository.UserRepository;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceTest {

    @Mock
    private AvatarService avatarService;

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AdsServiceImpl adsService;

    @Test
    void addAd() {
        String email = "test@example.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setAds(new ArrayList<>());
        Ads ads = new Ads();

        when(authService.getEmailFromAuthUser()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);

        adsService.addAd(ads);

        verify(authService).getEmailFromAuthUser();
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(userInfo);
        assertEquals(userInfo, ads.getAuthor());
    }

    @Test
    void getCommentByIdShouldReturnCommentIfExists() {
        Comment comment = new Comment();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = adsService.getCommentById(1L);

        assertEquals(comment, result);
    }

    @Test
    void updateAdShouldUpdateAdInRepository() {
        Ads ads = new Ads();

        adsService.updateAd(ads);

        verify(adsRepository).save(ads);
    }

    @Test
    void deleteAdd() {
        String email = "test@example.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setAds(new ArrayList<>());
        userInfo.setComments(new ArrayList<>());
        Ads ads = new Ads();
        ads.setComments(new ArrayList<>());
        userInfo.addAdFromUser(ads);

        when(authService.getEmailFromAuthUser()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);

        adsService.deleteAdd(ads);

        verify(authService).getEmailFromAuthUser();
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(userInfo);
        verify(commentRepository).deleteAll(ads.getComments());
        verify(adsRepository).delete(ads);
    }

    @Test
    void findById() {
        long id = 1L;
        Ads ads = new Ads();

        when(adsRepository.findById(id)).thenReturn(Optional.of(ads));

        Ads result = adsService.findById(id);

        verify(adsRepository).findById(id);
        assertEquals(ads, result);
    }

    @Test
    void addCommentToAd() {
        String email = "test@example.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setAds(new ArrayList<>());
        userInfo.setComments(new ArrayList<>());
        long id = 1L;
        Ads ads = new Ads();
        ads.setComments(new ArrayList<>());
        Comment comment = new Comment();

        when(authService.getEmailFromAuthUser()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);
        when(adsRepository.findById(id)).thenReturn(Optional.of(ads));

        adsService.addCommentToAd(comment, id);

        verify(authService).getEmailFromAuthUser();
        verify(userRepository).findByEmail(email);
        verify(adsRepository).findById(id);
        verify(commentRepository).save(comment);
        verify(adsRepository).save(ads);
    }

    @Test
    void deleteComment() {
        long adId = 1L;
        long commentId = 2L;
        UserInfo userInfo = new UserInfo();
        userInfo.setAds(new ArrayList<>());
        userInfo.setComments(new ArrayList<>());
        Ads ads = new Ads();
        ads.setAuthor(userInfo);
        ads.setComments(new ArrayList<>());
        Comment comment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(adsRepository.findById(adId)).thenReturn(Optional.of(ads));

        adsService.deleteComment(adId, commentId);

        verify(commentRepository).findById(commentId);
        verify(adsRepository).findById(adId);
        verify(userRepository).save(userInfo);
        verify(adsRepository).save(ads);
        verify(commentRepository).delete(comment);
    }

    @Test
    void updateComment() {
        long adId = 1L;
        long commentId = 2L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("updated text");
        Ads ads = new Ads();
        ads.setComments(new ArrayList<>());
        Comment comment = new Comment();
        comment.setAuthor(new UserInfo());
        ads.addComment(comment);

        when(adsRepository.findById(adId)).thenReturn(Optional.of(ads));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        CommentDto result = adsService.updateComment(adId, commentId, commentDto);

        verify(adsRepository).findById(adId);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
        assertEquals("updated text", result.getText());
    }

    @Test
    void mapListToCommentDto() {
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setText("text1");
        comment1.setAuthor(new UserInfo());
        Comment comment2 = new Comment();
        comment2.setText("text2");
        comment2.setAuthor(new UserInfo());
        comments.add(comment1);
        comments.add(comment2);

        List<CommentDto> result = adsService.mapListToCommentDto(comments);

        assertEquals(2, result.size());
        assertEquals("text1", result.get(0).getText());
        assertEquals("text2", result.get(1).getText());
    }

    @Test
    void getCommentDtoList() {
        long id = 1L;
        Ads ads = new Ads();
        ads.setComments(new ArrayList<>());
        Comment comment1 = new Comment();
        comment1.setText("text1");
        comment1.setAuthor(new UserInfo());
        Comment comment2 = new Comment();
        comment2.setText("text2");
        comment2.setAuthor(new UserInfo());
        ads.addComment(comment1);
        ads.addComment(comment2);

        when(adsRepository.findById(id)).thenReturn(Optional.of(ads));

        List<CommentDto> result = adsService.getCommentDtoList(id);

        verify(adsRepository).findById(id);
        assertEquals(2, result.size());
        assertEquals("text1", result.get(0).getText());
        assertEquals("text2", result.get(1).getText());
    }

    @Test
    void getAdsDtoListFromAuthUser() {
        String email = "test@example.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setAds(new ArrayList<>());
        Ads ads1 = new Ads();
        ads1.setTitle("title1");
        ads1.setAuthor(new UserInfo());
        Ads ads2 = new Ads();
        ads2.setTitle("title2");
        ads2.setAuthor(new UserInfo());
        userInfo.addAdFromUser(ads1);
        userInfo.addAdFromUser(ads2);

        when(authService.getEmailFromAuthUser()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);

        List<AdsDto> result = adsService.getAdsDtoListFromAuthUser();

        verify(authService).getEmailFromAuthUser();
        verify(userRepository).findByEmail(email);
        assertEquals(2, result.size());
        assertEquals("title1", result.get(0).getTitle());
        assertEquals("title2", result.get(1).getTitle());
    }



    @Test
    void getAdsFromAllUsers() {
        List<Ads> adsList = new ArrayList<>();
        Ads ads1 = new Ads();
        ads1.setTitle("title1");
        ads1.setAuthor(new UserInfo());
        Ads ads2 = new Ads();
        ads2.setTitle("title2");
        ads2.setAuthor(new UserInfo());
        adsList.add(ads1);
        adsList.add(ads2);

        when(adsRepository.findAll()).thenReturn(adsList);

        List<AdsDto> result = adsService.getAdsFromAllUsers();

        verify(adsRepository).findAll();
        assertEquals(2, result.size());
        assertEquals("title1", result.get(0).getTitle());
        assertEquals("title2", result.get(1).getTitle());
    }

   /* @Test
    void updateAdImageFromAuthUser() throws IOException {
        long id = 1L;
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image data".getBytes());
        Ads ads = new Ads();
        Avatar avatar = new Avatar();

        when(adsRepository.findById(id)).thenReturn(Optional.of(ads));
        when(avatarService.getAllAvatars()).thenReturn(Collections.singletonList(avatar));

        adsService.updateAdImageFromAuthUser(id, image);

        verify(adsRepository).findById(id);
        verify(avatarService).testSave(eq(image), eq(MediaType.IMAGE_JPEG));
        verify(avatarService).getAllAvatars();
        assertEquals(avatar,avatarService.testSave(image,MediaType.IMAGE_JPEG));
    }*/
}
