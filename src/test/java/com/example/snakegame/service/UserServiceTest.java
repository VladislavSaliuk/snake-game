package com.example.snakegame.service;


import com.example.snakegame.entity.User;
import com.example.snakegame.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = {UserService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;
    @Test
    void registerUser_shouldInsertUserToDataBase_whenInputContainsCorrectData(){
        User user = new User("Test login",  "Test password", 0);
        userService.registerUser(user.getLogin(),  user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_shouldReturnNull_whenInputContainsLoginAsNull(){
        User user = new User(null, "Test password", 0);
        userService.registerUser(user.getLogin(),  user.getPassword());
        verify(userRepository, never()).save(user);
    }


    @Test
    void registerUser_shouldReturnNull_whenInputContainsPasswordAsNull(){
        User user = new User("Test login",  null, 0);
        userService.registerUser(user.getLogin(), user.getPassword());
        verify(userRepository, never()).save(user);
    }
    @Test
    void authenticateUser_shouldFetchCorrectUserFromDatabase_whenInputContainsCorrectData(){
        String login = "Test login";
        String password = "Test password";
        User expectedUser = new User("Test login",  "Test password", 0);
        when(userRepository.findByLoginAndPassword(login ,password)).thenReturn(expectedUser);
        User actualUser = userService.authenticateUser(login, password);
        assertTrue(expectedUser.equals(actualUser));
        verify(userRepository).findByLoginAndPassword(login ,password);
    }

    @Test
    void authenticateUser_shouldReturnNull_whenInputContainsLoginAsNull(){
        String login = null;
        String password = "Test password";
        when(userRepository.findByLoginAndPassword(login ,password)).thenReturn(null);
        User actualUser = userService.authenticateUser(login, password);
        assertNull(actualUser);
        verify(userRepository).findByLoginAndPassword(login ,password);
    }

    @Test
    void authenticateUser_shouldReturnNull_whenInputContainsPasswordAsNull(){
        String login = "Test login";
        String password = null;
        when(userRepository.findByLoginAndPassword(login ,password)).thenReturn(null);
        User actualUser = userService.authenticateUser(login, password);
        assertNull(actualUser);
        verify(userRepository).findByLoginAndPassword(login ,password);
    }

}
