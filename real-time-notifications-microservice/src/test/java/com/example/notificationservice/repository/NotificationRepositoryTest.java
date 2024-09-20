package com.example.notificationservice.repository;

import com.example.notificationservice.entity.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
@DataMongoTest  //Este nos indica que esta va estar enfocado a la capa de persitencia de datos usando MongoDB
class NotificationRepositoryTest {


    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void findByUserReferenceId() {

        Notification notification1 = new Notification("123","user1","user1","mensaje1", Instant.now()); // Se crea 2 notificaciones simuladas
        Notification notification2 = new Notification("456","user1","user1","mensaje2", Instant.now()); // Se crea 2 notificaciones simuladas con el mismo usuario

        Flux<Notification> notificationFlux = notificationRepository.saveAll(Flux.just(notification1,notification2)); // Guarda Notificaciones en nuestro repositorio
        StepVerifier.create(notificationFlux)
                .expectNextCount(2)// se guarde las 2 notificaciones
                .verifyComplete();

        Flux<Notification> saveNotification = notificationRepository.findByUserReferenceId("user1");  // Llamar el metdod al repositroio para busacr las notificaciones

        StepVerifier.create(saveNotification) //Verifica que las notificaciones se este dando en un orden correcto
                .expectNext(notification1)
                .expectNext(notification2)
                .verifyComplete();


    }
}