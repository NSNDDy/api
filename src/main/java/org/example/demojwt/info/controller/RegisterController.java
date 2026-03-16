package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.common.dto.RegisterRequest;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        if (isBlank(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        if (isBlank(request.getUsername()) || request.getUsername().length() < 3 || request.getUsername().length() > 32) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username"));
        }
        if (isBlank(request.getPassword()) || request.getPassword().length() < 6 || request.getPassword().length() > 128) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid password"));
        }
        if (!isValidEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid email"));
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username already exists"));
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email already exists"));
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .avatar("https://ui-avatars.com/api/?name=" + request.getUsername())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", null));
    }
}
