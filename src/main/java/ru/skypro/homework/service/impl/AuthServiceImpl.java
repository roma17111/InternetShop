package ru.skypro.homework.service.impl;

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
import ru.skypro.homework.configurations.CustomUserDetailsService;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mdels.UserInfo;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
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
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(password.length());
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {
        if (userRepository.findByEmail(registerReqDto.getUsername()) != null) {
            return false;
        }
        if (manager.userExists(registerReqDto.getUsername())) {
            return false;
        }
        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReqDto.getPassword())
                        .username(registerReqDto.getUsername())
                        .roles(registerReqDto.getRole().name())
                        .build()
        );
        UserInfo userInfo = RegisterReqDto.mapToUserInfo(registerReqDto);
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.addRole(Role.USER);
        userRepository.save(userInfo);
        log.info("User added! " + userInfo);
        return true;
    }

    @Override
    public UserInfo getUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        for (UserInfo dto : usersDto) {
            if (principal.getUsername().equals(dto.getEmail())) {
                return dto;
            }
        }
        return null;
    }
}
