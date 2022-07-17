package com.example.cryptocurrencywatcher.service;

import com.example.cryptocurrencywatcher.entity.User;
import com.example.cryptocurrencywatcher.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private  final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
