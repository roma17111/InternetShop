package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mdels.UserInfo;

import java.util.List;

public interface AuthService {
    void addUser(RegisterReq user);

    List<RegisterReq> getAllUsers();

    boolean login(String userName, String password);
    boolean register(RegisterReq registerReq, Role role);

    UserInfo getUser();
}
