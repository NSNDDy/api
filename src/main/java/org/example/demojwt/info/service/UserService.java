package org.example.demojwt.info.service;

import lombok.RequiredArgsConstructor;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.stereotype.Service;

import org.example.demojwt.info.entity.User;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;

    public int getUser() {
        return userRepository.counAllUser();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
