package com.example.snakegame.service;

import com.example.snakegame.entity.User;
import com.example.snakegame.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User registerUser(String login, String password) {
        if (login == null  || password == null) {
            return null;
        } else {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setRecord(0);
            userRepository.save(user);
            return user;
        }
    }

    public User authenticateUser(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

}
