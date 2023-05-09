package ru.skypro.homework.service.mappers;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.models.UserInfo;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserInfoDto mapUserInfoToDto(UserInfo user);
    UserInfo mapUserDtoToInfo(UserInfoDto user);
}
