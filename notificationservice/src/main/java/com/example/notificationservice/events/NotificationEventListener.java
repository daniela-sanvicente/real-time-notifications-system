package com.example.notificationservice.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
/**
 * Class that listens for notification events and realizes something action when an event is received. 
 * This class uses the @EventListener to listen for events of type "NotificationEvent".
 */
@Component
public class NotificationEventListener {
    
    @EventListener
    public void handleNotificationEvent(NotificationEvent event){
        // Logic to handle the event
        System.out.println("Received notification event - userId: " +
        event.getUserId() + ", message: " + event.getMessage());
    }
}
