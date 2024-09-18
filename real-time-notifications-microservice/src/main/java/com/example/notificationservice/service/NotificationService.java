package com.example.notificationservice.service;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;


@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    // Crear una nueva notificación para un usuario
    public Mono<Notification> createNotification(String userId, Notification notification) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    notification.setTimestamp(Instant.now()); // Establece el timestamp actual
                    return notificationRepository.save(notification)
                            .flatMap(savedNotification -> {
                                user.getNotifications().add(savedNotification.getId()); // Agrega el ID al usuario
                                return userRepository.save(user).thenReturn(savedNotification);
                            });
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)));
    }

    // Obtener todas las notificaciones de un usuario por su ID (String)
    public Mono<User> getUserByIdWithNotificationMessagesNotification(String userId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    // Obtener las notificaciones del usuario
                    List<String> notificationIds = user.getNotifications();

                    // Buscar y mapear las notificaciones en lugar de los IDs
                    return Flux.fromIterable(notificationIds)
                            .flatMap(notificationRepository::findById)
                            .map(Notification::getMessage) // Convertir notificación a su mensaje
                            .collectList() // Recolectar los mensajes en una lista
                            .flatMap(messages -> {
                                user.setNotifications(messages); // Asignar los mensajes al usuario
                                return Mono.just(user); // Retornar el usuario con los mensajes
                            });
                });
    }


}
