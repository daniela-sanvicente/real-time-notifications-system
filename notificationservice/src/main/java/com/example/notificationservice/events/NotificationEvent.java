package com.example.notificationservice.events;

import lombok.Data;

@Data
public class NotificationEvent {
    private String userId;
    private String message;

    public NotificationEvent() {
    }

    public NotificationEvent(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
