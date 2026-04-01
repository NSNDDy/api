package org.example.demojwt.info.controller;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.info.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.example.demojwt.info.entity.User;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @RequestMapping(method = RequestMethod.GET,value = "/all")
    public int getCountUser() {
        return userService.getUser();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
