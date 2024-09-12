package com.example.notificationservice.events;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
/**
 * Class that publishes events. This class uses ApplicationEventPublisher
 * from Spring to publish events.
 */

@Component
public class NotificationEventPublisher {
    
    private final ApplicationEventPublisher publisher;

    /**
     * Constructs a new NotificationEventPublisher with the specified ApplicationEventPublisher.
     * 
     * @param publisher the ApplicationEventPublisher used to publish events
     */
    public NotificationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Publishes a notification event with the given user ID and message.
     * 
     * @param userId the ID of the user to send the notification to
     * @param message the message content of the notification
     */
    public void publishEvent(final String userId, final String message){
        NotificationEvent event = new NotificationEvent(userId, message);
        publisher.publishEvent(event);
    }
}
