package ru.skypro.homework.dto;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление, определяющее роли пользователей.
 * Реализует интерфейс GrantedAuthority для интеграции с системой безопасности Spring.
 */
public enum Role implements GrantedAuthority {
    /**
     * Роль пользователя.
     */
    USER,

    /**
     * Роль администратора.
     */
    ADMIN;

    /**
     * Возвращает имя роли в виде строки. Используется системой безопасности Spring.
     *
     * @return Имя роли.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
