package com.project.LMS.service;

import com.project.LMS.Repository.UserRepository;
import com.project.LMS.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public User updatePassword(Long id, String password) {
        User user=userRepository.findById(id).orElse(null);
        if(user!=null) {
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
        }
        return user;
    }
}
