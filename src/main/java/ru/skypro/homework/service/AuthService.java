package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mdels.UserInfo;

import java.util.List;

public interface AuthService {
    void addUser(RegisterReqDto user);

    List<RegisterReqDto> getAllUsers();

    boolean login(String userName, String password);
    boolean register(RegisterReqDto registerReqDto, Role role);

    UserInfo getUser();
}
