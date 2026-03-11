package org.example.demojwt.info.service;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;

    public int getUser() {
        return userRepository.counAllUser();
    }
}
