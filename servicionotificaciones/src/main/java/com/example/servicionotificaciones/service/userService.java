package com.example.servicionotificaciones.service;
import com.example.servicionotificaciones.entity.user;
import com.example.servicionotificaciones.repository.usuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class userService {
    @Autowired
    private usuarioRepositorio UserRepository;

    public Mono<user> createUser(user usuario){
        return UserRepository.save(usuario);
    }

    public Mono<user> getUserById(String id){
        return UserRepository.findById(id);
    }


}
