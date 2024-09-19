package com.example.notificationservice.service;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.repository.UserRepository;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    // Flujo de notificaciones para un usuario utilizando SSE
    public Flux<Notification> getNotificationsStream(String userId) {
        return Flux.interval(Duration.ofMinutes(1))  // Emitir un evento cada segundo
                .flatMap(tick -> notificationRepository.findByUserReferenceId(userId)
                        .doOnNext(notification -> System.out.println("Emitida notificación: " + notification.getMessage()))  // Log para ver qué se está emitiendo
                )
                .repeat();  // Mantener el flujo activo
    }


    // Crear una nueva notificación para un usuario
    public Mono<Notification> createNotification(String userId, Notification notification) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (notification.getId() == null || notification.getId().isEmpty()) {
                        notification.setId(UUID.randomUUID().toString().substring(0,6));  // Generar un ID único para la notificación
                    }
                    notification.setUserReferenceId(userId);  // Establece la referencia del usuario
                    notification.setTimestamp(Instant.now());  // Establece la marca de tiempo actual
                    return notificationRepository.save(notification)
                            .flatMap(savedNotification -> {
                                user.getNotifications().add(savedNotification.getMessage());  // Agrega la notificación al usuario
                                return userRepository.save(user).thenReturn(savedNotification);
                            });
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)));
    }

    // Obtener todas las notificaciones de un usuario por su ID
    public Mono<User> getUserByIdWithNotificationMessagesNotification(String userId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    // Obtener las notificaciones del usuario
                    List<String> notificationIds = user.getNotifications();

                    return Flux.fromIterable(notificationIds)
                            .flatMap(notificationRepository::findById)
                            .map(Notification::getMessage)
                            .collectList()
                            .flatMap(messages -> {
                                user.setNotifications(messages);
                                return Mono.just(user);
                            });
                });
    }

    // Metodo para eliminar una notificación del usuario cuando se marca como vista
    public Mono<String> deleteNotificationFromUser(String userId, String notificationId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)))
                .flatMap(user -> {
                    if (user.getNotifications().contains(notificationId)) {
                        // Eliminar la notificación de la lista del usuario
                        user.getNotifications().remove(notificationId);

                        // Guardar el usuario actualizado
                        return userRepository.save(user)
                                // Luego, eliminar la notificación de la base de datos de notificaciones
                                .then(notificationRepository.deleteById(notificationId))
                                // Retornar un mensaje de éxito
                                .thenReturn("La notificación con ID: " + notificationId + " ha sido leída y eliminada con éxito.");
                    } else {
                        // Si la notificación no existe en la lista de notificaciones del usuario
                        return Mono.error(new RuntimeException("Notificación no encontrada para el usuario con ID: " + userId));
                    }
                });
    }


}
