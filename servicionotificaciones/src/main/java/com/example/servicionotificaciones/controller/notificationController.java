package com.example.servicionotificaciones.controller;

import java.time.Instant;
import java.util.UUID;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.servicionotificaciones.dto.notificationDto;
import com.example.servicionotificaciones.entity.notification;
import com.example.servicionotificaciones.repository.notificationRepository;
import com.example.servicionotificaciones.service.notificationService;


@RestController
@RequestMapping("/notifications")
public class notificationController {
    @Autowired
    private notificationService servicioNotificaciones;

    @PostMapping
    public Mono<notification> createNotification(@RequestBody notificationDto notificacionDto){

       notification notificacion = new notification(UUID.randomUUID().toString(), notificacionDto.getUserId(), notificacionDto.getMessage(), Instant.now());
       /*notification notificacionchida = new notification();
       notificacionchida.setId(UUID.randomUUID().toString());
       notificacionchida.setUserId(notificacionDto.getUserId());
       notificacionchida.setMessage(notificacionDto.getMessage());
       notificacionchida.setTimestamp(Instant.now());*/

        return servicioNotificaciones.createNotification(notificacion);
        
    }

    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<notification>>streamNotifications(@PathVariable String userId){
        return servicioNotificaciones.getNotificationsForUser(userId)
        .map (notificacion -> ServerSentEvent.builder(notificacion).build()); 
    }

}
