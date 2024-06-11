package com.task.serviceA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.task.serviceA.dto.UserDto;
import com.task.serviceA.entity.User;
import com.task.serviceA.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    
    //here we save the user object if notify call @ serviceb hits other wise it go with fallback method.
    @Transactional
    @CircuitBreaker(name = "notifyCircuitBreaker", fallbackMethod = "fallbackNotify")
    @Retry(name = "notifyRetry")
    public void saveUser(UserDto userDto) throws Exception {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        userRepository.save(user);

        notifyServiceB(userDto);

        user.setNotified(true);
        userRepository.save(user);
    }

    private void notifyServiceB(UserDto userDto) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://service-b/notify", userDto, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to notify Service B");
        }
    }

    public void fallbackNotify(UserDto userDto, Exception e) {
        User user = userRepository.findByemail(userDto.getEmail());
        if (user != null) {
            user.setNotified(false);
            userRepository.save(user);
        }
        System.err.println("Failed to notify Service B, reason: " + e.getMessage());
        saveFailedNotification(userDto, e.getMessage());
    }

    private void saveFailedNotification(UserDto userDto, String reason) {
        System.out.println("Saving failed notification for user: " + userDto.getName() + ", reason: " + reason);
    }

}