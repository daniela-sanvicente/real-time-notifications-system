package com.example.notificationservice.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String userId;
    private String message;
    private Instant timestamp;
    
    public Notification(String id, String userId, String message, Instant timestamp) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    
}
