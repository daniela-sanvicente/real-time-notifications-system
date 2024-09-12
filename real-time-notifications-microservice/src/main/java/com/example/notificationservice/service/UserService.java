package com.example.notificationservice.service;

import com.example.notificationservice.entity.User;
import com.example.notificationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //obtener la lista de los usuraios
    public Flux<User> getAllUsers(){
        return userRepository.findAll();
    }
    //Obtener usuario por id
    public Mono<User> findById (String id){
        return userRepository.findById(id);
    }

    //Guardar un usuario
    public <S extends User>Mono<S> saveUser(S user){
        return userRepository.save(user);
    }

    //eliminar un usuario por ID
    public Mono<Void> deleteUserById(String id){
        return userRepository.deleteById(id);
    }

}