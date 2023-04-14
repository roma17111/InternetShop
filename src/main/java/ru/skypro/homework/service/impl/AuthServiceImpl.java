package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.AuthService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final static List<RegisterReq> users = new ArrayList<>();

    private final static List<UserDto> usersDto = new ArrayList<>();{
        usersDto.add(new UserDto("user@gmail.com",
                "Roman",
                "Yakimenko", "123456"));
    }

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;


    public AuthServiceImpl(UserDetailsManager manager) {
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    public static List<RegisterReq> getUsers() {
        return users;
    }

    @Override
    public void addUser(RegisterReq user) {
        users.add(user);
    }

    @Override
    public List<RegisterReq> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        if (manager.userExists(registerReq.getUsername())) {
            return false;
        }
        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(registerReq.getRole().name())
                        .build()
        );
        UserDto user = new UserDto(registerReq.getUsername(),
                registerReq.getFirstName(),
                registerReq.getLastName(),
                registerReq.getPhone());
        System.out.println(registerReq);
        addUser(registerReq);
        usersDto.add(user);
        return true;
    }

    @Override
    public UserDto getUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        for (UserDto dto : usersDto) {
            if (principal.getUsername().equals(dto.getEmail())) {
                return dto;
            }
        }
        return null;
    }
}
