package com.example.servicionotificaciones.repository;

import com.example.servicionotificaciones.entity.notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface notificationRepository extends ReactiveMongoRepository<notification, String>{
    Flux<notification> findByUserId(String userId);
}
