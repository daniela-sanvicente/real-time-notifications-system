package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-clients/v1.0/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;


    public NotificationController(NotificationService notificationService, NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/test/users/{userId}")
    public Flux<Notification> getNotificationsForUser(@PathVariable String userId) {
        return notificationRepository.findByUserReferenceId(userId);
    }

    // Endpoint para transmitir notificaciones en tiempo real (SSE)
    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> streamNotifications(@PathVariable String userId) {
        // Llamamos al servicio para obtener el flujo de notificaciones para el usuario
        return notificationService.getNotificationsStream(userId);
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

    // Metodo DELETE para marcar la notificación como vista (eliminarla del usuario)
    @DeleteMapping("/users/{userId}/notifications")
    public Mono<String> deleteNotification(@PathVariable String userId, @RequestBody String notificationId) {
        return notificationService.deleteNotificationFromUser(userId, notificationId);
    }

}
