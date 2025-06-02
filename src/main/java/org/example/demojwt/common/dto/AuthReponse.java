package org.example.demojwt.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthReponse {
    private String accessToken;
    private String refreshToken;
}
