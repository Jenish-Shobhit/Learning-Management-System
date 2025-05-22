package com.project.LMS.controller;

import com.project.LMS.Repository.UserRepository;
import com.project.LMS.exceptions.UserNotFoundException;
import com.project.LMS.model.User;
import com.project.LMS.response.StandardResponse;
import com.project.LMS.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

	@GetMapping
	public ResponseEntity<StandardResponse<List<User>>> getAllUsers() {
		try {

			logger.info("Fetching all users");
			List<User> users = userService.getAllUsers();
			StandardResponse<List<User>> response = new StandardResponse<>(HttpStatus.OK.value(), "Success", users);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching all users", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch users", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<StandardResponse<User>> getUserById(@PathVariable Long id) {
		try {
			logger.info("Fetching user with id: {}", id);
			User user = userService.getUserById(id);
			if (user != null) {
				return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", user),
						HttpStatus.OK);
			} else {
				logger.warn("User with id: {} not found", id);
				throw new UserNotFoundException("User not found");
			}
		} catch (UserNotFoundException e) {
			logger.warn("User with id: {} not found", id);
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null),
					HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error("Error fetching user with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch user", e);
		}
	}

	@PostMapping
	public ResponseEntity<StandardResponse<User>> createUser(@RequestBody User user) {
		try {
			logger.info("Creating new user");
			User savedUser = userService.saveUser(user);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.CREATED.value(), "User created successfully", savedUser),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating user", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create user", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<StandardResponse<Void>> deleteUser(@PathVariable Long id) {
		try {
			logger.info("Deleting user with id: {}", id);
			userService.deleteUser(id);
			StandardResponse<Void> response = new StandardResponse<>(HttpStatus.NO_CONTENT.value(),
					"User deleted successfully", null);
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error deleting user with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete user", e);
		}
	}






}
