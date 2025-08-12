package org.example.demojwt.lgn.service;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.common.dto.AuthReponse;
import org.example.demojwt.common.dto.AuthRequest;
import org.example.demojwt.common.dto.MessageInfoDto;
import org.example.demojwt.common.dto.ResponseInfoDto;
import org.example.demojwt.common.util.JwtUtil;
import org.example.demojwt.common.util.MsgUtil;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiLoginService {


    private final MsgUtil msgUtil;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;



    public ResponseInfoDto getInfoLogin(AuthRequest req) {

        ResponseInfoDto responseInfoDto = new ResponseInfoDto();
        MessageInfoDto messageInfoDto;

        User user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user.getUsername()).equals(req.getUsername())) {
            messageInfoDto = msgUtil.getMessage("ERROR",
                                                "MessageId1",
                                                "ID1",
                                                "Không tìm thấy User");

            responseInfoDto.setResult(ResponseInfoDto.NOT_FOUND_RESULT);
            responseInfoDto.getMessageList().add(messageInfoDto);
            return responseInfoDto;
        }

        messageInfoDto = msgUtil.getMessage("INFO",
                                            "MessageId1",
                                            "ID1",
                                            "AccessToken");
        responseInfoDto.setResult(ResponseInfoDto.SUCCESS_RESULT);
        responseInfoDto.getMessageList().add(messageInfoDto);
        responseInfoDto.setReturnObject(user);
        return responseInfoDto;
    }

    public HttpHeaders getHeaderForLogin(ResponseInfoDto responseInfoDto){
        HttpHeaders header = new HttpHeaders();

        if (responseInfoDto.getResult() != ResponseInfoDto.SUCCESS_RESULT){
            return header;
        }

        String accessToken = jwtUtil.getAccessToken((User) responseInfoDto.getReturnObject());

        header.add("accessToken",
                   accessToken);
        return header;
    }

}
