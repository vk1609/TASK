package com.task.serviceA.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.task.serviceA.dto.UserDto;
import com.task.serviceA.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // user details will be saved with the help of data transfer object UserDTO, if they saved after the transactional call 
    //completes other wise it will go for retry finally it will throw error if the retry wont work
    
    
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        try {
            userService.saveUser(userDto);
            return ResponseEntity.ok("User created and notification sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred: " + e.getMessage());
        }
    }
}