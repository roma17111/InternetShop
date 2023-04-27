package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.skypro.homework.configurations.CustomUserDetailsService;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AvatarService avatarService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void getById() {
        long id = 1L;
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(userInfo));

        UserInfo result = authService.getById(id);

        assertEquals(userInfo, result);
    }

    @Test
    void login() {
        String userName = "test@test.com";
        String password = "password";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userName);
        userInfo.setPassword(new BCryptPasswordEncoder().encode(password));
        when(userRepository.findByEmail(userName)).thenReturn(userInfo);
        when(customUserDetailsService.loadUserByUsername(userName)).thenReturn(new User(userName, userInfo.getPassword(), new ArrayList<>()));

        boolean result = authService.login(userName, password);

        assertTrue(result);
    }

    @Test
    void register() {
        RegisterReqDto registerReqDto = new RegisterReqDto();
        registerReqDto.setUsername("test@test.com");
        registerReqDto.setPassword("password");
        when(userRepository.findByEmail(registerReqDto.getUsername())).thenReturn(null);

        boolean result = authService.register(registerReqDto, Role.USER);

        assertTrue(result);
    }

    @Test
    void getEmailFromAuthUser() {
        String email = "test@test.com";
        UserDetails userDetails = new User(email, "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String result = authService.getEmailFromAuthUser();

        assertEquals(email, result);
    }

    @Test
    void getByUserName() {
        String userName = "test@test.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userName);
        when(userRepository.findByEmail(userName)).thenReturn(userInfo);

        UserInfo result = authService.getByUserName(userName);

        assertEquals(userInfo, result);
    }

    @Test
    void saveUser() {
        UserInfo userInfo = new UserInfo();
        authService.saveUser(userInfo);

        verify(userRepository).save(userInfo);
    }

    @Test
    void setPasswordFromUser() {
        String email = "test@test.com";
        String newPassword = "newPassword";
        UserDetails userDetails = new User(email, "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);

        authService.setPasswordFromUser(newPassword);

        verify(userRepository).save(userInfo);
    }

    @Test
    void userIsAdmin() {
        String email = "test@test.com";
        UserDetails userDetails = new User(email, "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setRole(Role.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);

        boolean result = authService.userIsAdmin();

        assertTrue(result);
    }

    @Test
    void updateAuthUser() {
        String email = "test@test.com";
        UserDetails userDetails = new User(email, "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(userInfo);
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setFirstName("firstName");
        userInfoDto.setLastName("lastName");
        userInfoDto.setPhone("1234567890");

        UserInfoDto result = authService.updateAuthUser(userInfoDto);

        assertEquals(userInfoDto.getFirstName(), result.getFirstName());
        assertEquals(userInfoDto.getLastName(), result.getLastName());
        assertEquals(userInfoDto.getPhone(), result.getPhone());
        verify(userRepository).save(userInfo);
    }
}
