package com.example.notificationservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.notificationservice.entity.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
