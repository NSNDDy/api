package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.*;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> me() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Unauthorized"));
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setAvatar(user.getAvatar());
        userDto.setEmail(user.getEmail());

        return ResponseEntity.ok(ApiResponse.success("User info", userDto));
    }
}
