package com.example.notificationservice.controller;

import com.example.notificationservice.HttpResponse.ApiResponse;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private NotificationService notificationService;

    @InjectMocks
    private UserController userController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(userController).build();
    }

    @Test
    void testDeleteUser_Success() {
        // Simulamos que el servicio elimina correctamente al usuario
        when(userService.deleteUserById(anyString())).thenReturn(Mono.just("Usuario eliminado con éxito"));

        // Realizamos la petición DELETE y verificamos la respuesta
        webTestClient.delete().uri("/api-clients/v1.0/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class)
                .consumeWith(response -> {
                    ApiResponse<?> apiResponse = response.getResponseBody();
                    assert apiResponse != null;
                    assert apiResponse.getStatus().equals("success");
                    assert apiResponse.getMessage().equals("Usuario eliminado con éxito");
                });
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Simulamos que el servicio no encuentra el usuario
        when(userService.deleteUserById(anyString())).thenReturn(Mono.error(new RuntimeException("Usuario no encontrado con ID: 1")));

        // Ejecuta la petición DELETE y verifica que el estado sea 404
        webTestClient.delete().uri("/api-clients/v1.0/users/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiResponse.class)
                .consumeWith(response -> {
                    ApiResponse<?> apiResponse = response.getResponseBody();
                    assert apiResponse != null;
                    assert apiResponse.getStatus().equals("error");
                    assert apiResponse.getMessage().equals("Usuario no encontrado con ID: 1");
                });
    }

}