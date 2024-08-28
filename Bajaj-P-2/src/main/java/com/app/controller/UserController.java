package com.app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import com.app.entities.User;
import com.app.service.UserService;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    

    @PostMapping("/create")    
    public ResponseEntity<String> createUser(@RequestHeader(value = "roll-number", required = true) String rollNumber,
                                             @Valid @RequestBody User user) {
        try {
            // Check if the roll number is provided
            if (rollNumber == null || rollNumber.isEmpty()) {
                return new ResponseEntity<>("Roll number is required", HttpStatus.UNAUTHORIZED);
            }
            
            // Create the user
            return userService.createUser(rollNumber, user);
           
        } catch (IllegalArgumentException e) {
            // Return bad request if phone number or email is duplicate
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
