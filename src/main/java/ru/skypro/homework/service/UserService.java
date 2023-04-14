package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;

public interface UserService {
    void addUser(User user);

    User getUserByEmail(String email);
}
