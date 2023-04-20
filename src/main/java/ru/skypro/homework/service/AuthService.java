package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.models.UserInfo;

import java.util.List;

public interface AuthService {
    void addUser(RegisterReqDto user);

    List<RegisterReqDto> getAllUsers();

    UserInfo getById(long id);

    boolean login(String userName, String password);
    boolean register(RegisterReqDto registerReqDto, Role role);


    String getEmailFromAuthUser();

    UserInfo getByUserName(String userName);

    void saveUser(UserInfo user);

    void setPasswordFromUser(String newPassword);
}
