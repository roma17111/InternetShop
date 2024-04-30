package ru.skypro.homework.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.skypro.homework.dto.Role;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация веб-безопасности приложения.
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Сервис для работы с информацией о пользователях.
     */
    private final CustomUserDetailsService userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register",
            "/ads",
            "/ads/comments/avatars2/**",
            "/ads/avatars2/**"//это аватары изображений объявлений
    };

    /**
     * Конфигурирует менеджер аутентификации с сервисом пользователей и кодировщиком паролей.
     *
     * @param auth менеджер аутентификации
     * @throws Exception если возникает ошибка при настройке параметров безопасности
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Конфигурирует параметры безопасности HTTP-запросов.
     *
     * @param http объект для настройки параметров безопасности
     * @throws Exception если возникает ошибка при настройке параметров безопасности
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                        authz
                                .mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/ads/**", "/users/**")
                                .authenticated()
                )
                .cors(Customizer.withDefaults())
                .httpBasic(withDefaults()).userDetailsService(userDetailsService);

    }

    /**
     * Создает и возвращает объект для кодирования паролей.
     *
     * @return кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}

