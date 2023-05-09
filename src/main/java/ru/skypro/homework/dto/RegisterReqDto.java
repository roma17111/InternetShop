package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.models.UserInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role = Role.USER;

    public static UserInfo mapToUserInfo(RegisterReqDto reqDto) {
        return new UserInfo(reqDto.getUsername(),
                reqDto.getPassword(),
                reqDto.getFirstName(),
                reqDto.getLastName(),
                reqDto.getPhone(),
                reqDto.getRole());
    }
}
