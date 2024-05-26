package com.example.snakegame.service;

import com.example.snakegame.model.User;
import com.example.snakegame.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = bCryptPasswordEncoder.encode(password);

            User user = new User();
            user.setLogin(login);
            user.setPassword(encryptedPassword);
            user.setRecord(0);
            userRepository.save(user);
            return user;
        }
    }

    public User authenticateUser(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                user.setPassword(password);
                return user;
            }
        }
        return null;
    }

    public void updateRecord(String login, long newRecord) {

        if(login == null) {
            throw new IllegalArgumentException("Login is null!");
        }

        User user = userRepository.findByLogin(login);

        if (user != null) {
            if (newRecord > user.getRecord()) {
                user.setRecord(newRecord);
                userRepository.save(user);
                System.out.println("Record updated successfully for user: " + login);
            } else {
                System.out.println("New record is not higher than current record for user: " + login);
            }
        } else {
            System.out.println("User not found with login: " + login);
        }
    }



}
