package org.example.demojwt.lgn.controller;

import org.example.demojwt.common.dto.AuthRequest;
import org.example.demojwt.common.dto.ResponseInfoDto;
import org.example.demojwt.lgn.service.ApiLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiLoginController {

    @Autowired
    ApiLoginService apiLoginService;


    @PostMapping(value = "/api-login")
    public ResponseEntity<Object> apiLogin(@RequestBody
                                           AuthRequest authRequest){

        ResponseInfoDto responseInfoDto = apiLoginService.getInfoLogin(authRequest);

        HttpHeaders header = apiLoginService.getHeaderForLogin(responseInfoDto);

        return new ResponseEntity<>(responseInfoDto, header,
                                    HttpStatus.OK);
    }

}
