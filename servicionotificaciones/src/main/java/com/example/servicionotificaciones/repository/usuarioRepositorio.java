package com.example.servicionotificaciones.repository;
import com.example.servicionotificaciones.entity.user;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

@Repository
public interface usuarioRepositorio extends ReactiveMongoRepository <user,String>{

}
