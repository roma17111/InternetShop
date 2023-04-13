package ru.skypro.homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.impl.UserServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register"
    };


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        for (User user : UserServiceImpl.getUsers()) {
            UserDetails user1 = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
            return new InMemoryUserDetailsManager(user1);
        }
        UserDetails user = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                .username("user@gmail.com")
                .password("password")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                        authz
                                .mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/ads/**", "/users/**").authenticated()

                )
                .cors().disable()
                .httpBasic(withDefaults());
        return http.build();
    }


}

