package com.example.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
