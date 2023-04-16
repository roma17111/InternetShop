package ru.skypro.homework.dto;

public enum Role {
    USER, ADMIN;

    public String getAuthority() {
        return name();
    }
}
