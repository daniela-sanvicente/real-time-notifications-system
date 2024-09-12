package com.example.notificationservice.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.utils.DateUtils;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
public class NotificationRepositoryTest {
    // Test implementation

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    void testSaveAndFindNotificationByUserId(){
        Notification notification = new Notification("1", "user1", "New message", DateUtils.now());//Se crea una nueva notificación
        notificationRepository.save(notification).block();//block() se utiliza para bloquear el hilo actual y esperar a que se complete la operación de guardado

        Flux<Notification> result = notificationRepository.findByUserId("user1");//Se busca la notificación por ID de usuario
        StepVerifier.create(result)//StepVerifier es una herramienta de prueba de Reactor para probar flujos reactivos
        .expectNext(notification)//Se espera que la notificación devuelta sea la misma que la notificación original
        .verifyComplete();//Se verifica que el flujo se complete correctamente
    }
}
