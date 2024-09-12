package com.example.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.events.NotificationEventPublisher;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.utils.DateUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationEventPublisher eventPublisher;
    
    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testCreateNotification() {
        // Test implementation
        Notification notification = new Notification("1", "user1", "New message", DateUtils.now());
        Mockito.when(notificationRepository.save(notification)).thenReturn(Mono.just(notification));//Se simula el guardado de la notificación y con thenReturn se devuelve un Mono con la notificación guardada

        Mono<Notification> result = notificationService.createNotification(notification);//Se llama al método createNotification de NotificationService, que debería guardar la notificación y publicar un evento
        StepVerifier.create(result)//Se verifica que el resultado sea correcto, StepVerifier es una herramienta de prueba de Reactor para probar flujos reactivos
        .expectNext(notification)//Se espera que la notificación devuelta sea la misma que la notificación original
        .verifyComplete();//Se verifica que el flujo se complete correctamente

        Mockito.verify(eventPublisher, Mockito.times(1))//Se verifica que el método publishEvent del NotificationEventPublisher se haya llamado exactamente una vez con los parámetros correctos
        .publishEvent(notification.getUserId(), notification.getMessage());//Se verifica que el evento se haya publicado correctamente
    }

    @Test
    void testGetNotificationsByUserId(){
        Notification notification = new Notification("1", "user1", "New message", DateUtils.now());
        Mockito.when(notificationRepository.findByUserId("user1")).thenReturn(Flux.just(notification));//Se simula la búsqueda de notificaciones por ID de usuario y con thenReturn se devuelve un Flux con la notificación

        Flux<Notification> result = notificationService.getNotificationsForUser("user1");//Se llama al método getNotificationsForUser de NotificationService, que debería devolver las notificaciones para el usuario
        StepVerifier.create(result)//StepVerifier es una herramienta de prueba de Reactor para probar flujos reactivos
        .expectNext(notification)//Se espera que la notificación devuelta sea la misma que la notificación original
        .verifyComplete();//Se verifica que el flujo se complete correctamente
    }
    
}
