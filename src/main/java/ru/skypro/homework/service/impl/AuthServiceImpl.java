package ru.skypro.homework.service.impl;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.configurations.CustomUserDetailsService;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.UserInfo;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j
@Service
public class AuthServiceImpl implements AuthService {

    private final static List<RegisterReqDto> users = new ArrayList<>();

    private final static List<UserInfo> usersDto = new ArrayList<>();

    {
        usersDto.add(new UserInfo("user@gmail.com",
                "Roman",
                "Yakimenko", "123456"));
    }

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    public AuthServiceImpl(UserDetailsManager manager, UserRepository userRepository, CustomUserDetailsService customUserDetailsService) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.encoder = new BCryptPasswordEncoder();
    }

    public static List<RegisterReqDto> getUsers() {
        return users;
    }

    @Override
    public void addUser(RegisterReqDto user) {
        users.add(user);
    }

    @Override
    public List<RegisterReqDto> getAllUsers() {
        return Collections.unmodifiableList(users);
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
        if (userInfo.getRole().equals(Role.ADMIN)) {
            return true;
        } else {
            return false;
        }
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

    @Override
    public void updateUserImage(MultipartFile image) {
        String email = getEmailFromAuthUser();
        UserInfo userInfo = getByUserName(email);
        try {
            userInfo.setImage(image.getBytes());
        } catch (IOException e) {
            log.error("invalid file - " + e.getMessage());
        }
        saveUser(userInfo);
    }
}
