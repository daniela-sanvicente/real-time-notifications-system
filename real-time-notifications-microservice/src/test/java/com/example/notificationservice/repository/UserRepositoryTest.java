package com.example.notificationservice.repository;

import com.example.notificationservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;


@DataMongoTest // Este se asegura de que los repositorios de Mongo se configuren correctamente
@ContextConfiguration(classes = {com.example.notificationservice.RealTimeNotificationsMicroserviceApplication.class}) // Asegura que se cargue la configuración del contexto
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository; //  bean inyectado

    @Test
    void testFindUserAndNotifications() {
        // Crear usuario con un array de notificaciones
        List<String> notifications = Arrays.asList(
                "Tu pedido sera entregado en 12hrs",
                "Tu pedido sera entregado en 1hr"
        );
        User user = new User("14dadd", "Juan Spert", "juan.spert@example.com", notifications);

        // Guardar usuario
        Mono<User> userMono = userRepository.save(user); // Aquí guardamos el usuario en MongoDB

        // Verificar que el usuario fue guardado correctamente
        StepVerifier.create(userMono)
                .assertNext(savedUser -> {
                    assert savedUser.getName().equals("Juan Spert");
                    assert savedUser.getNotifications().size() == 2;
                })
                .verifyComplete();

        // Buscar el usuario por su id y verificar sus notificaciones
        Mono<User> foundUserMono = userRepository.findById("14dadd");

        StepVerifier.create(foundUserMono)
                .assertNext(foundUser -> {
                    assert foundUser.getName().equals("Juan Spert");
                    assert foundUser.getNotifications().contains("Tu pedido sera entregado en 12hrs");
                    assert foundUser.getNotifications().contains("Tu pedido sera entregado en 1hr");
                })
                .verifyComplete();
    }
}