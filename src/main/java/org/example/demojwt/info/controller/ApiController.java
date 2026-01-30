package org.example.demojwt.info.controller;

import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/secure")
    public String secure(){
        return "\uD83D\uDD12 Đây là dữ liệu bí mật!";
    }

    @GetMapping("/username")
    public String username(Authentication authentication){
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow().getUsername();
    }
}
