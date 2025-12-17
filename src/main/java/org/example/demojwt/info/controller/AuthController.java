package org.example.demojwt.info.controller;

import org.example.demojwt.common.dto.AuthRequest;
import org.example.demojwt.common.dto.ResponseInfoDto;
import org.example.demojwt.common.util.MsgUtil;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.example.demojwt.common.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MsgUtil msgUntil;


    @PostMapping("/register")
    public String register(@RequestBody
                           AuthRequest authRequest){
        User user = User.builder()
                .userId(null)
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(1)
                .build();
//                new User(null , authRequest.getUsername(), passwordEncoder.encode(authRequest.getPassword()), 1, null);
        userRepository.save(user);
        return "Đăng ký thành công";
    }

    @PostMapping("/login")
    public ResponseInfoDto login(@RequestBody AuthRequest authRequest){

        ResponseInfoDto responseInfoDto = new ResponseInfoDto();
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Sai Thông tin"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
            throw new RuntimeException("Sai mật khẩu");


        String accessToken = jwtService.generateToken(user.getUsername());




        return responseInfoDto;
//        String refreshToken = jwtService.generateToken(user.getUsername());
//
//
//
//        user.setRefreshToken(refreshToken);
//        userRepository.save(user);
//        return new AuthReponse(accessToken,refreshToken);
    }

//    @PostMapping("/refresh")
//    public AuthResponse refreshToken(@RequestParam String refreshToken) {
//        if (!jwtService.validateToken(refreshToken))
//            throw new RuntimeException("Refresh token không hợp lệ");
//
//        String username = jwtService.extractUsername(refreshToken);
//        User user = userRepo.findByUsername(username).orElseThrow();
//
//        if (!refreshToken.equals(user.getRefreshToken()))
//            throw new RuntimeException("Token không khớp");
//
//        String newAccessToken = jwtService.generateToken(username, 15 * 60 * 1000);
//        return new AuthResponse(newAccessToken, refreshToken);
//    }

}
