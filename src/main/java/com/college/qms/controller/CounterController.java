package com.college.qms.controller;

import com.college.qms.dto.ApiResponse;
import com.college.qms.dto.CounterRequest;
import com.college.qms.dto.CounterResponse;
import com.college.qms.service.CounterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counters")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CounterController {

    @Autowired
    private CounterService counterService;

    private void verifyAdmin(String role) {
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            throw new SecurityException("Access denied: Only ADMINISTRATOR can modify counters!");
        }
    }

    // 1. GET all counters (Public)
    @GetMapping
    public ResponseEntity<List<CounterResponse>> getAllCounters() {
        return ResponseEntity.ok(counterService.getAllCounters());
    }

    // 2. GET counter by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<CounterResponse> getCounterById(@PathVariable Long id) {
        return ResponseEntity.ok(counterService.getCounterById(id));
    }

    // 3. POST create new counter (ADMIN only)
    @PostMapping
    public ResponseEntity<CounterResponse> createCounter(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody CounterRequest request) {
        verifyAdmin(role);
        CounterResponse created = counterService.createCounter(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // 4. PUT update existing counter (ADMIN only)
    @PutMapping("/{id}")
    public ResponseEntity<CounterResponse> updateCounter(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody CounterRequest request) {
        verifyAdmin(role);
        CounterResponse updated = counterService.updateCounter(id, request);
        return ResponseEntity.ok(updated);
    }

    // 5. DELETE counter (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCounter(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyAdmin(role);
        counterService.deleteCounter(id);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Counter deleted successfully with ID: " + id));
    }
}
