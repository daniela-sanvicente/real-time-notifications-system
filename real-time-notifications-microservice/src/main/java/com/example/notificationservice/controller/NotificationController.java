package com.example.notificationservice.controller;

import com.example.notificationservice.HttpResponse.ApiResponse;
import com.example.notificationservice.HttpResponse.ResponseUtil;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api-clients/v1.0/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }
    // Obtener todas las notificaciones de un usuario
    @GetMapping("/users/{userId}")
    public Mono<ResponseEntity<ApiResponse<List<Notification>>>> getUserNotifications(@PathVariable String userId) {
        return notificationService.getUserNotifications(userId);
    }

    // Transmitir notificaciones en tiempo real (SSE)
    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> streamNotifications(@PathVariable String userId) {
        return notificationService.getNotificationsStream(userId);
    }

    // Crear una notificación para un usuario
    @PostMapping("/users/{userId}")
    public Mono<ResponseEntity<ApiResponse<Notification>>> createNotification(@PathVariable String userId, @RequestBody Notification notification) {
        return notificationService.createNotification(userId, notification)
                .flatMap(createdNotification -> ResponseUtil.createSuccessResponse("Notificación creada con éxito", createdNotification))
                .onErrorResume(e -> ResponseUtil.createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    // Eliminar una notificación del usuario
    @DeleteMapping("/users/{userId}/notifications")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteNotification(@PathVariable String userId, @RequestBody String notificationMessage) {
        return notificationService.deleteNotificationFromUser(userId, notificationMessage)
                .flatMap(successMessage -> ResponseUtil.createSuccessResponse(successMessage, (Void) null))
                .onErrorResume(e -> ResponseUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND));
    }

}
