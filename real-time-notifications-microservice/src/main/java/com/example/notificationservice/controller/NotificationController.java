package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-clients/v1.0/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    // Metodo POST para crear una notificación para un usuario
    @PostMapping("/users/{userId}")
    public Mono<Notification> createNotification(@PathVariable String userId, @RequestBody Notification notification) {
        return notificationService.createNotification(userId, notification);
    }

    // Metodo GET para obtener todas las notificaciones de un usuario

    @GetMapping("/users/{userId}")
    public Mono<User> getUserByIdWithNotifications(@PathVariable String userId) {
        return notificationService.getUserByIdWithNotificationMessagesNotification(userId);
    }

}
