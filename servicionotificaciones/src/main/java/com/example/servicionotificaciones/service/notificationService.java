package com.example.servicionotificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.servicionotificaciones.entity.notification;
import com.example.servicionotificaciones.repository.notificationRepository;

@Service
public class notificationService {
    @Autowired
    private notificationRepository notificacionRepositorio;

    public  Mono<notification> createNotification(notification notificacion){
        return notificacionRepositorio.save(notificacion);
    }

    public Flux<notification> getNotificationsForUser(String userId) {
        return notificacionRepositorio.findByUserId(userId);
    }
}
