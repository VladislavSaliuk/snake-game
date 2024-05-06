package com.example.snakegame.repository;

import com.example.snakegame.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldInsertUserToDataBase_whenInputContainsUser(){
        User user = new User("Test login", "Test email", "Test password" ,0);
        userRepository.save(user);
        assertEquals(11,userRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldInsertUserToDataBase_whenInputContainsUserWithOutLogin(){
        User user = new User(null, "Test email", "Test password" ,0);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldInsertUserToDataBase_whenInputContainsUserWithOutEmail(){
        User user = new User("Test login", null, "Test password" ,0);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldInsertUserToDataBase_whenInputContainsUserWithOutPassword(){
        User user = new User("Test login", "Test email", null ,0);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void findByLoginAndPassword_shouldReturnCorrectUser_whenInputContainsExistingLoginAndPassword(){
        String login = "john_doe";
        String password = "password123";
        User expectedUser = new User(1, "john_doe","john_doe@example.com", "password123",0);
        User actualUser = userRepository.findByLoginAndPassword(login,password);
        assertTrue(expectedUser.equals(actualUser));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void findByLoginAndPassword_shouldReturnNull_whenInputContainsNotExistingLoginAndPassword(){
        String login = "Some login";
        String password = "Some password";
        User actualUser = userRepository.findByLoginAndPassword(login,password);
        assertNull(actualUser);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void findByLoginAndPassword_shouldReturnNull_whenInputContainsLoginAsNull(){
        String login = null;
        String password = "Some password";
        User actualUser = userRepository.findByLoginAndPassword(login,password);
        assertNull(actualUser);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void findByLoginAndPassword_shouldReturnNull_whenInputContainsPasswordAsNull(){
        String login = "Some login";
        String password = null;
        User actualUser = userRepository.findByLoginAndPassword(login,password);
        assertNull(actualUser);
    }

}
