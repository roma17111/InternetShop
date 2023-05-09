package ru.skypro.homework.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для добавления заголовка "Access-Control-Allow-Credentials" с значением "true" к каждому HTTP-запросу.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Обрабатывает каждый HTTP-запрос, добавляет заголовок "Access-Control-Allow-Credentials" с значением "true" и передает запрос дальше по цепочке фильтров.
     *
     * @param httpServletRequest  входящий HTTP-запрос
     * @param httpServletResponse исходящий HTTP-ответ
     * @param filterChain         цепочка фильтров для обработки запроса
     * @throws ServletException если возникает ошибка в процессе обработки запроса
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
