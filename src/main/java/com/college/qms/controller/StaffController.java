package com.college.qms.controller;

import com.college.qms.dto.TokenResponse;
import com.college.qms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StaffController {

    @Autowired
    private StaffService staffService;

    private void verifyStaff(String role) {
        if (role == null || (!"STAFF".equalsIgnoreCase(role.trim()) && !"ADMIN".equalsIgnoreCase(role.trim()))) {
            throw new SecurityException("Access denied: Only STAFF can access staff queue operations!");
        }
    }

    // 1. GET /api/staff/queue/{serviceId} - View waiting queue
    @GetMapping("/queue/{serviceId}")
    public ResponseEntity<List<TokenResponse>> getWaitingQueue(
            @PathVariable Long serviceId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyStaff(role);
        List<TokenResponse> waitingTokens = staffService.getWaitingQueue(serviceId);
        return ResponseEntity.ok(waitingTokens);
    }

    // 2. POST /api/staff/queue/{serviceId}/next - Call the next available token
    @PostMapping("/queue/{serviceId}/next")
    public ResponseEntity<TokenResponse> callNextToken(
            @PathVariable Long serviceId,
            @RequestParam(value = "counterId", required = false) Long counterId,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyStaff(role);
        TokenResponse response = staffService.callNextToken(serviceId, counterId);
        return ResponseEntity.ok(response);
    }

    // 3. PUT /api/staff/tokens/{id}/complete - Complete service for token
    @PutMapping("/tokens/{id}/complete")
    public ResponseEntity<TokenResponse> completeToken(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyStaff(role);
        TokenResponse response = staffService.completeToken(id);
        return ResponseEntity.ok(response);
    }

    // 4. PUT /api/staff/tokens/{id}/skip - Skip a token
    @PutMapping("/tokens/{id}/skip")
    public ResponseEntity<TokenResponse> skipToken(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyStaff(role);
        TokenResponse response = staffService.skipToken(id);
        return ResponseEntity.ok(response);
    }
}
