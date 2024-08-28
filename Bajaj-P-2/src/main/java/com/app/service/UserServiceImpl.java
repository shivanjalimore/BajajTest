package com.app.service;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.app.entities.User;
import com.app.respository.UserRepo;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired UserRepo userRepo;

	    @Override
	    public ResponseEntity<String> createUser(String rollNumber, User user) {
	    	RestTemplate restTemplate = new RestTemplate();
	    	 final String API_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";
	        // Check for duplicate phone number
	        if (userRepo.findByPhoneNumber(user.getPhoneNumber()) != null) {
	            throw new IllegalArgumentException("Phone number already exists");
	        }

	        // Check for duplicate email
	        if (userRepo.findByEmailId(user.getEmailId()) != null) {
	            throw new IllegalArgumentException("Email already exists");
	        }

	        // Set headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("roll-number", rollNumber);

	        // Set body
	        HttpEntity<User> request = new HttpEntity<>(user, headers);

	        // Send POST request
	        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

	        // Check response status
	        if (response.getStatusCode().is2xxSuccessful()) {
	            // Save the user to the database
	            userRepo.save(user);
	            System.out.println("User with roll number: " + rollNumber + " created successfully!");
	        } else {
	            throw new RuntimeException("Failed to create user: " + response.getStatusCode());
	        }
			return response;
	    }

	
}
