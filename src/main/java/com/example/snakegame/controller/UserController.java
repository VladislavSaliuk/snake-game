package com.example.snakegame.controller;

import com.example.snakegame.entity.User;
import com.example.snakegame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new User());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new User());
        return "login_page";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user){
        User registeredUser = userService.registerUser(user.getLogin() , user.getPassword());
        return registeredUser == null ? "error_page" : "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model){
        User authenticatedUser = userService.authenticateUser(user.getLogin(), user.getPassword());
        if(authenticatedUser != null){
            model.addAttribute("login", authenticatedUser.getLogin());
            model.addAttribute("record", authenticatedUser.getRecord());
            return "game_page";
        } else {
            return "error_page";
        }
    }

    @PostMapping("/updateRecord")
    @ResponseBody
    public String updateRecord(@RequestParam String login, @RequestParam long newRecord) {
        userService.updateRecord(login, newRecord);
        return "Record updated";
    }


}
