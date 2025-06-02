package org.example.demojwt.info.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/secure")
    public String secure(){
        return "\uD83D\uDD12 Đây là dữ liệu bí mật!";
    }
}
