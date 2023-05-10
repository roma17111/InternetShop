package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.models.UserInfo;

/**
 * Класс представляющий DTO (Data Transfer Object) для регистрации нового пользователя.
 * Используется для передачи данных между слоями приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqDto {

    /**
     * Имя пользователя.
     */
    private String username;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Номер телефона пользователя.
     */
    private String phone;

    /**
     * Роль пользователя. По умолчанию - Пользователь (USER).
     */
    private Role role = Role.USER;

    /**
     * Метод для преобразования данных пользователя из RegisterReqDto в UserInfo.
     *
     * @param reqDto Данные пользователя в формате RegisterReqDto.
     * @return Данные пользователя в формате UserInfo.
     */
    public static UserInfo mapToUserInfo(RegisterReqDto reqDto) {
        return new UserInfo(reqDto.getUsername(),
                reqDto.getPassword(),
                reqDto.getFirstName(),
                reqDto.getLastName(),
                reqDto.getPhone(),
                reqDto.getRole());
    }
}