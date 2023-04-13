package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.Role;

import java.util.List;

public interface UserService {
    void addUser(User user);

    List<User> getAllUsers();

    boolean isExistsUser(String userName);

    User findUserByUserName(String UserName);

    boolean login(String userName, String password);
    boolean register(User user);
}
