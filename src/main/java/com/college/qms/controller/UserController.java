package com.college.qms.controller;

import com.college.qms.dto.TokenResponse;
import com.college.qms.dto.UserResponse;
import com.college.qms.repository.UserRepository;
import com.college.qms.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UserController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private void verifyAdmin(String role) {
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            throw new SecurityException("Access denied: Only ADMINISTRATOR can view users list!");
        }
    }

    // GET /api/users - View all registered users (ADMIN only)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyAdmin(role);
        List<UserResponse> list = userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // GET /api/users/{userId}/tokens - View customer token history
    @GetMapping("/{userId}/tokens")
    public ResponseEntity<List<TokenResponse>> getUserTokens(@PathVariable Long userId) {
        List<TokenResponse> tokenHistory = tokenService.getTokensByUserId(userId);
        return ResponseEntity.ok(tokenHistory);
    }
}
