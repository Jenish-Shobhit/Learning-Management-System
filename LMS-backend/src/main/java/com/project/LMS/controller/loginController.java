package com.project.LMS.controller;

import com.project.LMS.Repository.UserRepository;
import com.project.LMS.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.LMS.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class loginController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        logger.info("Registration attempt for user: {}", user.getEmail());
        try {
            userService.register(user);
            logger.info("User registered successfully: {}", user.getEmail());
            return user;
        } catch (Exception e) {
            logger.error("Error registering user: {} - {}", user.getEmail(), e.getMessage());
            throw e; // Re-throw the exception so that Spring's default exception handling can manage it.
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> customLogin(@RequestBody Map<String, String> credentials) {
        String useremail = credentials.get("useremail");
        String password = credentials.get("password");
        Map<String, String> response = new LinkedHashMap<>();

        if (useremail == null || password == null) {
            response.put("message", "Username and password are required.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        System.out.println("useremail: "+useremail);
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(useremail));

        if (userOptional.isEmpty()) {
            response.put("message", "Invalid username.");
            response.put("userId", "null");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();


        // Compare the provided password (raw) with the stored encrypted password
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Authentication successful
            // Here you would typically generate a session or JWT token

            response.put("message", "Login successful!");
            response.put("userId", user.getUserId().toString());
            response.put("role", user.getRole());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Authentication failed
            response.put("message", "Invalid password.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/emailexists")
    public ResponseEntity<Map<String, String>> emailExists(@RequestParam String useremail) {
        Map<String, String> response = new LinkedHashMap<>();

        if (useremail == null) {
            response.put("message", "Username is required.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        System.out.println("useremail: "+useremail);
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(useremail));

        if (userOptional.isEmpty()) {
            response.put("message", "Invalid username.");
            response.put("userId", "null");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        response.put("message", "email exists!");
        response.put("userId", user.getUserId().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updatePassword")
    public User updatePassword(@RequestBody Map<String, String> credentials) {
        Long idLong = Long.parseLong(credentials.get("userId"));
        String password = credentials.get("password");
        return userService.updatePassword(idLong, password);
    }
}
