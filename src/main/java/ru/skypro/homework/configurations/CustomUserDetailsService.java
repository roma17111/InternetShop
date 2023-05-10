package ru.skypro.homework.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.service.repository.UserRepository;

/**
 * Реализация пользовательского сервиса для работы с деталями пользователей в контексте Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Репозиторий пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Загружает информацию о пользователе по его имени пользователя (email).
     *
     * @param username имя пользователя (email)
     * @return объект UserDetails, содержащий информацию о пользователе
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}