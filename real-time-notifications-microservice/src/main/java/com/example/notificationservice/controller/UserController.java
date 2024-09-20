package com.example.notificationservice.controller;

import com.example.notificationservice.HttpResponse.ApiResponse;
import com.example.notificationservice.HttpResponse.ResponseUtil;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api-clients/v1.0/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
    }

    // Obtener todos los usuarios con sus notificaciones
    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<User>>>> getAllUsers() {
        return userService.getAllUsersWithNotificationMessages()
                .collectList() // Recoger todos los usuarios en una lista
                .flatMap(users -> {
                    if (users.isEmpty()) {
                        // Devolver una respuesta de error con tipo compatible
                        return ResponseUtil.createErrorResponse("No se encontraron usuarios.", HttpStatus.NOT_FOUND);
                    }
                    // Devolver una respuesta exitosa con la lista de usuarios
                    return ResponseUtil.createSuccessResponse("Usuarios encontrados", users);
                });
    }

    //Guardar un usuario
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<User>>> saveUser(@RequestBody User user) {
        return userService.saveUser(user)
                .flatMap(savedUser -> ResponseUtil.createSuccessResponse("Usuario guardado con éxito.", savedUser))
                .onErrorResume(e -> ResponseUtil.createErrorResponse("Error al guardar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    //Eliminar un usuario
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteUser(@PathVariable String id) {
        return userService.deleteUserById(id)
                .flatMap(successMessage -> ResponseUtil.createSuccessResponse(successMessage, (Void) null))  // Respuesta de éxito con tipo Void
                .onErrorResume(e -> ResponseUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND));  // Respuesta de error
    }



//mono sirve para los restpost y flux para los get, responseEntity es para manejar las respuestas de los servicios
}