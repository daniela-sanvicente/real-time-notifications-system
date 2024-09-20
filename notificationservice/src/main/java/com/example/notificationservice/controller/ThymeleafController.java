package com.example.notificationservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.service.NotificationService;


@Controller
public class ThymeleafController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotificationPage(Model model) {
        List<Notification> notifications = notificationService.getAllNotifications().collectList().block();
        model.addAttribute("notifications", notifications);
        return "notifications";
    }
    
}
