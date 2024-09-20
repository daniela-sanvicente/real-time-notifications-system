package com.example.notificationservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.events.NotificationEventPublisher;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.utils.DateUtils;

import reactor.core.publisher.Mono;
@WebFluxTest(controllers = NotificationController.class)
@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    // Test implementation
    @Mock
    private WebTestClient webTestClient;
    
    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void testCreateNotification() {
        // Test implementation
        NotificationDto notificationDto = new NotificationDto("user123", "Hello");
        Notification notification = new Notification("1", "user123", "Hello", DateUtils.now());

        Mockito.when(notificationService.createNotification(Mockito.any(Notification.class))).thenReturn(Mono.just(notification));

        webTestClient.post()
        .uri("/notifications")
        .bodyValue(notificationDto)
        .exchange()// este paso es necesario para que se ejecute la petición
        .expectStatus().isOk()
        .expectBody(Notification.class)
        .isEqualTo(notification);
        
    }
    
}
