package com.task.serviceB.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.serviceB.dto.NotificationDto;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @PostMapping
    public ResponseEntity<String> notify(@RequestBody NotificationDto notificationDto) {
        System.out.println("Received notification for user: " + notificationDto.getName());
        return ResponseEntity.ok("Notification received");
    }
}