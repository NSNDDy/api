package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.ApiResponse;
import org.example.demojwt.common.dto.AuthRequest;
import org.example.demojwt.common.dto.LoginResponse;
import org.example.demojwt.common.dto.UserDto;
import org.example.demojwt.common.service.JwtService;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody AuthRequest request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Username and password are required"));
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid username or password"));
        }

        String accessToken = jwtService.generateToken(user.getUsername());

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setAvatar(user.getAvatar());
        userDto.setEmail(user.getEmail());

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .user(userDto)
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    }
}
