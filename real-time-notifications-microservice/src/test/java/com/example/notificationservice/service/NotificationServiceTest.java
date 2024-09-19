package com.example.notificationservice.service;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateNotification() {
        String userId = "user1";
        User user = new User(); //crear un usuario
        Notification notification = new Notification("1", userId, "user1", "New message", Instant.now()); //crea una notificación
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user)); //Simula la búsqueda de un usuario por Id
        Mockito.when(notificationRepository.save(notification)).thenReturn(Mono.just(notification)); //Simula el guardado de una notificación
        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user)); //Simula el guardado de un usuario

        Mono<Notification> createNotification = notificationService.createNotification(userId, notification); //Llama al método del servicio
        StepVerifier.create(createNotification)// Verificar que sea reactivo (Mono)
                .expectNext(notification) // que contenga la notificación esperada
                .verifyComplete(); //que se complete correctamente
        verify(userRepository, times(1)).findById(userId); //verifica que los métodos del repositorio se llamen correctamente
        verify(notificationRepository, times(1)).save(notification); //verifica que el método salvar del repositorio se llame correctamente
    }

    @Test
    void testGetNotificationsStream() {
        String userId = "user1";
        Notification notification = new Notification("1", userId, "user1", "New message", Instant.now()); //crea una notificación
        Mockito.when(notificationRepository.findByUserReferenceId(userId)).thenReturn(Flux.just(notification)); //Configuramos un mock, para que cuando llame el método devuelva un flux con notificaciones simuladas

        Flux<Notification> getNotificationFlux = notificationService.getNotificationsStream(userId); //Llama al metodo del service y obtiene el flujo de notificaciones
        StepVerifier.create(getNotificationFlux)// Verificar que sea reactivo (Flux)
                .expectNext(notification) // que contenga la notificación esperada
                .verifyComplete(); //que se complete correctamente
        verify(notificationRepository, times(1)).findByUserReferenceId(userId);
    }


}