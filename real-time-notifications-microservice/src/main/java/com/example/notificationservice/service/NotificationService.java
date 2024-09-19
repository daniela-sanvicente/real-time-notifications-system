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
                                user.getNotifications().add(savedNotification.getMessage()); // Agrega el msj al usuario
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
