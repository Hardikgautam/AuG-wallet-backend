package com.prac_icsd2.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac_icsd2.model.User;
import com.prac_icsd2.repo.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") 
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 1. SIGNUP API - Saves user to Oracle DB
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check if email is already taken
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists!"));
        }
        
        // Save the user object directly
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    // 2. LOGIN API - Validates credentials
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOptional = userRepository.findByEmail(email);

        // Check if user exists and password matches
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            User user = userOptional.get();
            
            // Return safe data only (No password)
            return ResponseEntity.ok(Map.of(
                "message", "Login Successful",
                "fname", user.getFname(),
                "lname", user.getLname(),
                "email", user.getEmail()
            )); 
        } else {
            // Return 401 Unauthorized if login fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "Invalid email or password"));
        }
    }
}