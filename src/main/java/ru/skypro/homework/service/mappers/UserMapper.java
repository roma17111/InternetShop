package ru.skypro.homework.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UserInfoDto;
import ru.skypro.homework.mdels.UserInfo;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserInfoDto mapUserInfoToDto(UserInfo user);
    UserInfo mapUserDtoToInfo(UserInfoDto user);
}
