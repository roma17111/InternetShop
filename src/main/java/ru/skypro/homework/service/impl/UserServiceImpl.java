package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final static List<User> users = new ArrayList<>();

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;



    public UserServiceImpl(UserDetailsManager manager) {
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    public static List<User> getUsers() {
        return users;
    }
    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public boolean isExistsUser(String userName) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User findUserByUserName(String userName) {
        for (User user : getAllUsers()) {
            if (userName.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
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
    public boolean register(User user) {
        if (findUserByUserName(user.getUsername())!=null) {
            return false;
        }
        user.getRole().add(Role.USER);

        System.out.println(user);
        addUser(user);
        return true;
    }
}
