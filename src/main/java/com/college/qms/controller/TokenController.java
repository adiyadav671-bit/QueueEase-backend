package com.college.qms.controller;

import com.college.qms.dto.TokenRequest;
import com.college.qms.dto.TokenResponse;
import com.college.qms.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TokenController {

    @Autowired
    private TokenService tokenService;

    // 1. POST /api/tokens - Generate a new queue token
    @PostMapping
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest request) {
        TokenResponse response = tokenService.generateToken(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. GET /api/tokens/{id} - View token by ID
    @GetMapping("/{id}")
    public ResponseEntity<TokenResponse> getTokenById(@PathVariable Long id) {
        TokenResponse response = tokenService.getTokenById(id);
        return ResponseEntity.ok(response);
    }

    // 3. GET /api/tokens/user/{userId} - View all tokens for a customer
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TokenResponse>> getTokensByUser(@PathVariable Long userId) {
        List<TokenResponse> tokens = tokenService.getTokensByUserId(userId);
        return ResponseEntity.ok(tokens);
    }

    // 4. GET /api/tokens/{id}/position - View real-time queue position and status
    @GetMapping("/{id}/position")
    public ResponseEntity<com.college.qms.dto.TokenPositionResponse> getTokenPosition(@PathVariable Long id) {
        return ResponseEntity.ok(tokenService.getTokenPosition(id));
    }

    // 5. PUT /api/tokens/{id}/cancel - Cancel a waiting token
    @PutMapping("/{id}/cancel")
    public ResponseEntity<TokenResponse> cancelToken(
            @PathVariable Long id,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        Long effectiveUserId = userId != null ? userId : headerUserId;
        return ResponseEntity.ok(tokenService.cancelToken(id, effectiveUserId));
    }

    // 6. GET /api/tokens - View all tokens
    @GetMapping
    public ResponseEntity<List<TokenResponse>> getAllTokens() {
        return ResponseEntity.ok(tokenService.getAllTokens());
    }
}
