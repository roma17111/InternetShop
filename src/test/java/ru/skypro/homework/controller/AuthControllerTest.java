package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.LoginReq;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.skypro.homework.dto.Role.USER;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLoginSuccess() {
        LoginReq req = new LoginReq();
        req.setUsername("testuser");
        req.setPassword("testpass");

        when(authService.login(req.getUsername(), req.getPassword())).thenReturn(true);

        ResponseEntity<?> response = authController.login(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLoginFailure() {
        LoginReq req = new LoginReq();
        req.setUsername("testuser");
        req.setPassword("testpass");

        when(authService.login(req.getUsername(), req.getPassword())).thenReturn(false);

        ResponseEntity<?> response = authController.login(req);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testRegisterSuccess() {
        RegisterReqDto req = new RegisterReqDto();
        req.setUsername("testuser");
        req.setPassword("testpass");

        when(authService.register(req, USER)).thenReturn(true);

        ResponseEntity<?> response = authController.register(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterFailure() {
        RegisterReqDto req = new RegisterReqDto();
        req.setUsername("testuser");
        req.setPassword("testpass");

        when(authService.register(req, USER)).thenReturn(false);

        ResponseEntity<?> response = authController.register(req);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
