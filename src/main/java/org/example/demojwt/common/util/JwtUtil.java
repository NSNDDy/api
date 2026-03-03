/*
package org.example.demojwt.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.demojwt.common.dto.AuthRequest;
import org.example.demojwt.info.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    String secretKey;


    public String getAccessToken(User authRequest){
        //
        String accessToken = JWT.create()
                .withIssuer("Test")
                .withClaim("USERNAME",
                           authRequest.getUsername())
                .withExpiresAt(new Date())
                .sign(Algorithm.HMAC512(secretKey));
        //
        return accessToken;
    }





}
*/
