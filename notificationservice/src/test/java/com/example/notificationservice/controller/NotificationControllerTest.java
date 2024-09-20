package com.example.notificationservice.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.utils.DateUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@WebFluxTest(controllers = NotificationController.class)
@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    // Test implementation
    @Autowired
    private WebTestClient webTestClient;
    
    @Mock
    private NotificationService notificationService;

    //@Mock
    //private NotificationEventPublisher eventPublisher;

    //@InjectMocks
    //private NotificationController notificationController;

    @Test
    void testCreateNotification() {
        Notification notification = new Notification("1", "user123", "Hello", DateUtils.now());
        when(notificationService.createNotification(Mockito.any(Notification.class))).thenReturn(Mono.just(notification));

        webTestClient.post()
            .uri("/notifications")
            .bodyValue(new NotificationDto("user123", "Hello"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(Notification.class)
            .isEqualTo(notification);
    }

    @Test
    void testStreamNotifications() {
        // Crear una notificación de prueba
        Notification notification = new Notification("1", "user123", "Hello", DateUtils.now());

        // Simular el comportamiento del servicio de notificación
        Mockito.when(notificationService.getNotificationsForUser("user123")).thenReturn(Flux.just(notification));

        // Realizar la solicitud GET al endpoint de stream de notificaciones
        webTestClient.get()
            .uri("/notifications/stream/user123")
            .exchange() // Ejecutar la solicitud
            .expectStatus().isOk() // Verificar que la respuesta tiene un estado HTTP 200 (OK)
            .expectBodyList(Notification.class) // Esperar que el cuerpo de la respuesta sea una lista de notificaciones
            .hasSize(1) // Verificar que la lista tiene un elemento
            .contains(notification); // Verificar que la notificación de prueba está en la lista
    }
    
}
