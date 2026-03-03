package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.*;
import org.example.demojwt.common.service.JwtService;
import org.example.demojwt.common.mapper.UserMapper;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username already exists"));
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .avatar("https://ui-avatars.com/api/?name=" + request.getUsername()) // Default avatar
                .build();

        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid username or password"));
        }

        String accessToken = jwtService.generateToken(user.getUsername());
        
        UserDto userDto = userMapper.toDto(user);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .user(userDto)
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = userMapper.toDto(user);

        return ResponseEntity.ok(ApiResponse.success("User info", userDto));
    }
}
