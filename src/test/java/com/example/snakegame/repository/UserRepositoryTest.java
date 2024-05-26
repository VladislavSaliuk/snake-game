package com.example.snakegame.repository;

import com.example.snakegame.model.User;
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
        User user = new User("Test login",  "Test password" ,0);
        userRepository.save(user);
        assertEquals(11,userRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldThrowDataIntegrityViolationException_whenDublicatesLogin(){
        User user1 = new User("Test login 1", "Test password" ,0);
        User user2 = new User("Test login 1",  "Test password" ,0);
        userRepository.save(user1);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldThrowDataIntegrityViolationException_whenInputContainsUserWithOutLogin(){
        User user = new User(null,  "Test password" ,0);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void save_shouldThrowDataIntegrityViolationException_whenInputContainsUserWithOutPassword(){
        User user = new User("Test login",  null ,0);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_data.sql"})
    void findByLoginAndPassword_shouldReturnCorrectUser_whenInputContainsExistingLoginAndPassword(){
        String login = "john_doe";
        String password = "password123";
        User expectedUser = new User(1, "john_doe", "password123",0);
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
