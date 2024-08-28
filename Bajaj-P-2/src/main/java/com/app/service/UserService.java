package com.app.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.app.entities.User;

public interface UserService {


	ResponseEntity<String> createUser(String rollNumber, @Valid User user);



}
