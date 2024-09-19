package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //inicialización de mocks y webTestClient
        webTestClient = WebTestClient.bindToController(notificationController).build();
    }

    @Test
    void testStreamNotifications() {
        String userId = "user1";
        Notification notification = new Notification("1", userId,"user1", "New message", Instant.now());
        Mockito.when(notificationService.getNotificationsStream("user1")).thenReturn(Flux.just(notification));

        webTestClient.get()
                .uri("/api-clients/v1.0/notifications/stream/user1")//uri del endpoint
                .accept(MediaType.TEXT_EVENT_STREAM) //Respuesta en formato stream
                .exchange()//envía solicitud al endpoint
                .expectStatus().isOk()//nos muestra el status (200, 404, 500, etc)
                .expectBodyList(Notification.class) //verifica que sea una lista de notificaciones
                .hasSize(1)
                .consumeWith(response -> {
                    Notification notificationResponse =response.getResponseBody().get(0);
                    assert notificationResponse.getMessage().equals(notification.getMessage());
                    verify(notificationService,times(1)).getNotificationsStream(userId);
                });
    }

    @Test
    void testCreateNotification() {
        String userId = "user1";
        Notification notification = new Notification("1", userId, "user1", "New message", Instant.now());
        Mockito.when(notificationService.createNotification(eq(userId), any(Notification.class)))
                .thenReturn(Mono.just(notification));

        webTestClient.post()//solicitud post
                .uri("/api-clients/v1.0/notifications/users/user1", userId) //uri del endpoint
                .contentType(MediaType.APPLICATION_JSON)  //verificar que es de contenido JSON
                .bodyValue(notification) //definimos el cuerpo de la solicitud con la notificación simulada
                .exchange()
                .expectStatus().isOk()
                .expectBody(Notification.class) //verifica que el cuerpo de la respuesta tenga la notificación esperada
                .isEqualTo(notification);
        verify(notificationService,times(1)).createNotification(eq(userId),any(Notification.class)); //verifica que el método que está en el servicio haya sido llamado correctamente
    }
}