package com.app.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {

	 // Find user by phone number
    User findByPhoneNumber(String phoneNumber);

    // Find user by email
    User findByEmailId(String emailId);

}
