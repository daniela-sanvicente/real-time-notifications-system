package com.example.notificationservice.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String id;
    private String userId;
    private String message;
    private String timestamp;

    public NotificationDto() {
    }

    public NotificationDto(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

}
