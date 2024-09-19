package com.example.notificationservice.controller;

import com.example.notificationservice.entity.User;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-clients/v1.0/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
    }

    //Obtener todos los usuarios
    @GetMapping
    public Flux<User> getAllUsersWithNotifications() {
        return userService.getAllUsersWithNotificationMessages();
    }

    //Guardar un usuario
    @PostMapping
    public <S extends User> Mono<S> saveUSer(@RequestBody S user) {
        return userService.saveUser(user);
    }

    //Eliminar un usuario
    @DeleteMapping("/{id}")
    public Mono<Void> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }


}