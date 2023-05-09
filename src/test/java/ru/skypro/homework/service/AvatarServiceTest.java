package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.service.impl.AvatarServiceImpl;
import ru.skypro.homework.service.repository.AvatarRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:test.properties")
public class AvatarServiceTest {

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private AvatarServiceImpl avatarService;

    @Test
    void getAllAvatarsTest() {
        List<Avatar> avatars = new ArrayList<>();
        when(avatarRepository.findAll()).thenReturn(avatars);

        List<Avatar> result = avatarService.getAllAvatars();

        assertEquals(avatars, result);
    }
}
