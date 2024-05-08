package com.example.snakegame.repository;

import com.example.snakegame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByLoginAndPassword(String login, String password);
    User findByLogin(String login);


}
