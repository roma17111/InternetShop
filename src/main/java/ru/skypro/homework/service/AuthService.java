package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;

import java.util.List;

public interface AuthService {
    List<RegisterReq> getAllUsers();

    boolean login(String userName, String password);
    boolean register(RegisterReq registerReq, Role role);
}
