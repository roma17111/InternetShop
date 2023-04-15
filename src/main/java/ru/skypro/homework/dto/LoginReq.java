package ru.skypro.homework.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

@Data
public class LoginReq  {
    private String password;
    private String username;

}
