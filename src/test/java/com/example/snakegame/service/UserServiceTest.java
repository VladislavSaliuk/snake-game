package com.example.snakegame.service;


import com.example.snakegame.model.User;
import com.example.snakegame.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
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
        String login = "testLogin";
        String password = "testPassword";
        User user = new User();
        user.setLogin(login);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRecord(0);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.registerUser(login, password);
        assertNotNull(result);
        assertEquals(login, result.getLogin());
        assertTrue(new BCryptPasswordEncoder().matches(password, result.getPassword()));
        assertEquals(0, result.getRecord());
        verify(userRepository, times(1)).save(any(User.class));

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
    public void authenticateUser_shouldReturnNull_whenUserDoesNotExist() {
        String login = "Test logon";
        String password = "Test password";
        when(userRepository.findByLogin(login)).thenReturn(null);
        User result = userService.authenticateUser(login, password);
        assertNull(result);
    }

    @Test
    public void authenticateUser_shouldReturnNull_whenPasswordDoesNotMatch() {
        String login = "Test login";
        String password = "Test password";
        String encryptedPassword = new BCryptPasswordEncoder().encode("Correct password");
        User user = new User();
        user.setLogin(login);
        user.setPassword(encryptedPassword);
        when(userRepository.findByLogin(login)).thenReturn(user);
        User result = userService.authenticateUser(login, password);
        assertNull(result);
    }

    @Test
    public void authenticateUser_shouldReturnUser_whenPasswordMatches() {
        String login = "Test logon";
        String password = "Test password";
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        User user = new User();
        user.setLogin(login);
        user.setPassword(encryptedPassword);
        when(userRepository.findByLogin(login)).thenReturn(user);
        User result = userService.authenticateUser(login, password);
        assertNotNull(result);
        assertEquals(login, result.getLogin());
        assertEquals(password, result.getPassword());
    }

    @Test
    public void authenticateUser_shouldReturnNull_whenLoginIsNull() {
        String password = "Test password";
        User user = userService.authenticateUser(null, password);
        assertNull(user);
    }

    @Test
    public void authenticateUser_shouldReturnNull_whenPasswordIsNull() {
        String login = "Test login";
        User user = userService.authenticateUser(login, null);
        assertNull(user);
    }

    @Test
    public void updateRecord_shouldNotUpdateRecord_whenUserNotFound() {
        String login = "Test login";
        long newRecord = 100;
        when(userRepository.findByLogin(login)).thenReturn(null);
        userService.updateRecord(login, newRecord);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateRecord_shouldNotUpdateRecord_whenNewRecordIsNotHigher() {
        String login = "Test login";
        long currentRecord = 150;
        long newRecord = 100;
        User user = new User();
        user.setLogin(login);
        user.setRecord(currentRecord);
        when(userRepository.findByLogin(login)).thenReturn(user);
        userService.updateRecord(login, newRecord);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateRecord_shouldUpdateRecord_whenNewRecordIsHigher() {
        String login = "Test login";
        long currentRecord = 100;
        long newRecord = 150;
        User user = new User();
        user.setLogin(login);
        user.setRecord(currentRecord);
        when(userRepository.findByLogin(login)).thenReturn(user);
        userService.updateRecord(login, newRecord);
        assertEquals(newRecord, user.getRecord());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateRecord_shouldThrowException_whenLoginIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateRecord(null, 228));
        assertEquals(exception.getMessage(), "Login is null!");
        verify(userRepository, never()).findByLogin(null);
    }

}
