package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final List<User> users = new ArrayList<>();
    private final AuthService authService;
    private final UserDetailsManager manager;

    @Override
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        } else {
            log.error("Пользователь " + user + " уже существует");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (email.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }
}
