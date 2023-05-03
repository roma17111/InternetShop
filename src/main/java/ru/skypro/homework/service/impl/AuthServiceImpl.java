package ru.skypro.homework.service.impl;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.configurations.CustomUserDetailsService;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AvatarService avatarService;
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    public AuthServiceImpl(AvatarService avatarService,
                           UserRepository userRepository,
                           CustomUserDetailsService customUserDetailsService) {
        this.avatarService = avatarService;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserInfo getById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean login(String userName, String password) {
        if (userRepository.findByEmail(userName) == null) {
            return false;
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        return encoder.matches(password, encryptedPassword);
    }

    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {
        if (userRepository.findByEmail(registerReqDto.getUsername()) != null) {
            return false;
        }
        UserInfo userInfo = RegisterReqDto.mapToUserInfo(registerReqDto);
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        log.info("User registered! " + userInfo);
        return true;
    }

    @Override
    public String getEmailFromAuthUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        log.info("Получен email - " + principal.getUsername());
        return principal.getUsername();
    }

    @Override
    public UserInfo getByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }

    @Override
    public void saveUser(UserInfo user) {
        userRepository.save(user);
    }

    @Override
    public void setPasswordFromUser(String newPassword) {
        String email = getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        userInfo.setPassword(encoder.encode(newPassword));
        userRepository.save(userInfo);
    }

    @Override
    public boolean userIsAdmin() {
        String email = getEmailFromAuthUser();
        UserInfo userInfo = userRepository.findByEmail(email);
        return userInfo.getRole().equals(Role.ADMIN);
    }

    @Override
    public UserInfoDto updateAuthUser(UserInfoDto userInfoDto) {
        String userName = getEmailFromAuthUser();
        UserInfo userInfo = getByUserName(userName);
        userInfo.setFirstName(userInfoDto.getFirstName());
        userInfo.setLastName(userInfoDto.getLastName());
        userInfo.setPhone(userInfoDto.getPhone());
        log.info("User updated!!! " + userInfo.getEmail());
        saveUser(userInfo);
        return UserInfo.mapToUserInfoDto(userInfo);
    }

    @SneakyThrows
    @Override
    public void updateUserImage(MultipartFile image) {
        String email = getEmailFromAuthUser();
        UserInfo userInfo = getByUserName(email);
        byte[] i = image.getBytes();
        userInfo.setImage(i);
        saveUser(userInfo);
        Avatar avatar = avatarService.testSave(image, MediaType.parseMediaType(Objects.requireNonNull(image.getContentType())));
        userInfo.setAvatar(avatar);
        saveUser(userInfo);
    }
}
