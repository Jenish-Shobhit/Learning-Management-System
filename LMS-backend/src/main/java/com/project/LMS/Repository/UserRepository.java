package com.project.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.LMS.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String useremail);
}