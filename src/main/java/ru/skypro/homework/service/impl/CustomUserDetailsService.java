package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserServiceImpl userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByUserName(username);
    }
}
