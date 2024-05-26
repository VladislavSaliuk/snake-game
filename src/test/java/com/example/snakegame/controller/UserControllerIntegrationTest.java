package com.example.snakegame.controller;

import com.example.snakegame.model.User;
import com.example.snakegame.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getRegisterPage_shouldReturnRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register_page"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    public void getLoginPage_shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login_page"))
                .andExpect(model().attributeExists("loginRequest"));
    }

    @Test
    public void register_shouldRedirectToLogin_whenRegistrationIsSuccessful() throws Exception {
        User user = new User();
        user.setLogin("Test login");
        user.setPassword("Test password");

        when(userService.registerUser(user.getLogin(), user.getPassword())).thenReturn(user);

        mockMvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void register_shouldReturnErrorPage_whenRegistrationFails() throws Exception {
        User user = new User();
        user.setLogin("Test login");
        user.setPassword("Test password");

        when(userService.registerUser(user.getLogin(), user.getPassword())).thenReturn(null);

        mockMvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("error_page"));
    }

    @Test
    public void login_shouldReturnGamePage_whenAuthenticationIsSuccessful() throws Exception {
        User user = new User();
        user.setLogin("Test login");
        user.setPassword("Test password");

        User authenticatedUser = new User();
        authenticatedUser.setLogin("Test login");
        authenticatedUser.setPassword("Test password");
        authenticatedUser.setRecord(0);

        when(userService.authenticateUser(user.getLogin(), user.getPassword())).thenReturn(authenticatedUser);

        mockMvc.perform(post("/login")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("game_page"))
                .andExpect(model().attributeExists("login"))
                .andExpect(model().attributeExists("record"));
    }

    @Test
    public void login_shouldReturnErrorPage_whenAuthenticationFails() throws Exception {
        User user = new User();
        user.setLogin("Test login");
        user.setPassword("Test password");

        when(userService.authenticateUser(user.getLogin(), user.getPassword())).thenReturn(null);

        mockMvc.perform(post("/login")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("error_page"));
    }

    @Test
    public void updateRecord_shouldReturnSuccessMessage() throws Exception {
        String login = "Test login";
        long newRecord = 100;

        doNothing().when(userService).updateRecord(login, newRecord);

        mockMvc.perform(post("/updateRecord")
                        .param("login", login)
                        .param("newRecord", String.valueOf(newRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string("Record updated"));

        verify(userService, times(1)).updateRecord(login, newRecord);
    }


}
