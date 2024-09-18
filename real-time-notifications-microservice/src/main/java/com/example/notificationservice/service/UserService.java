package com.example.notificationservice.service;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public UserService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }
    public Flux<User> getAllUsersWithNotificationMessages() {
        return userRepository.findAll()
                .flatMap(user -> {
                    List<String> notificationIds = user.getNotifications();

                    // Obtener las notificaciones por sus IDs
                    return Flux.fromIterable(notificationIds)
                            .flatMap(notificationRepository::findById)
                            .map(Notification::getMessage) // Extraer solo el mensaje de la notificación
                            .collectList() // Coleccionar los mensajes en una lista
                            .flatMap(messages -> {
                                user.setNotifications(messages); // Reemplazar los IDs con los mensajes
                                return Mono.just(user); // Devolver el usuario con las notificaciones transformadas
                            });
                });
    }

    //Guardar un usuario
    public <S extends User>Mono<S> saveUser(S user){
        return userRepository.save(user);
    }

    //eliminar un usuario por ID
    public Mono<Void> deleteUserById(String id){
        return userRepository.deleteById(id);
    }

}