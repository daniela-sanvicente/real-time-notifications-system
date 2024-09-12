package com.example.notificationservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.notificationservice.entity.Notification;

import reactor.core.publisher.Flux;

@Repository
public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    Flux<Notification> findByUserId(String userId);
}
