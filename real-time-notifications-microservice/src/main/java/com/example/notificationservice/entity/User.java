package com.example.notificationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")

public class User {
    @Id
    private String id;

    private String name;
    private String email;

    private List<String> notifications = new ArrayList<>();


    //List para notificaciones


}
