package com.college.qms.service;

import com.college.qms.dto.AuthResponse;
import com.college.qms.dto.LoginRequest;
import com.college.qms.dto.RegisterRequest;
import com.college.qms.entity.User;
import com.college.qms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();

        // 1. Check duplicate email
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("Email is already registered! Please login.");
        }

        // 2. Determine role — allow CUSTOMER or STAFF, default to CUSTOMER
        String role = "CUSTOMER";
        if (request.getRole() != null) {
            String requestedRole = request.getRole().trim().toUpperCase();
            if (requestedRole.equals("STAFF") || requestedRole.equals("CUSTOMER")) {
                role = requestedRole;
            }
            // ADMIN cannot self-register — always rejected
        }

        // 3. Encode password using BCrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 4. Create user
        User newUser = new User(
                request.getName().trim(),
                cleanEmail,
                hashedPassword,
                role
        );

        User savedUser = userRepository.save(newUser);

        String message = role.equals("STAFF")
                ? "Staff account registered successfully!"
                : "Customer registered successfully!";

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                message
        );
    }

    public AuthResponse login(LoginRequest request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();

        // 1. Find user by email
        User user = userRepository.findByEmail(cleanEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password!"));

        // 2. Verify password (BCrypt or fallback for seed data)
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches && request.getPassword().equals(user.getPassword())) {
            // Auto-upgrade plain text password to BCrypt hash
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            matches = true;
        }

        if (!matches) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Login successful!"
        );
    }
}
