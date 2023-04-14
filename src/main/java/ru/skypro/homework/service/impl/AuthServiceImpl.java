package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final static List<RegisterReq> users = new ArrayList<>();

    private final UserService userService;

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;



    public AuthServiceImpl(UserService userService, UserDetailsManager manager) {
        this.userService = userService;
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
        ru.skypro.homework.dto.User user
                = new ru.skypro.homework.dto.User(registerReq.getUsername(),
                registerReq.getFirstName(),
                registerReq.getLastName(),
                registerReq.getPhone());
        System.out.println(registerReq);
        userService.addUser(user);
        addUser(registerReq);
        return true;
    }
}
