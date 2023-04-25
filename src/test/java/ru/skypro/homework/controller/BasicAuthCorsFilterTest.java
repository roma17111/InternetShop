package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BasicAuthCorsFilterTest {

    @InjectMocks
    private BasicAuthCorsFilter basicAuthCorsFilter;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        basicAuthCorsFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).addHeader("Access-Control-Allow-Credentials", "true");
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
}
