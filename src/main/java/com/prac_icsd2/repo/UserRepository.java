package com.prac_icsd2.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prac_icsd2.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // This is used for Login to find the user by their email
    Optional<User> findByEmail(String email);
}
