package com.college.qms.controller;

import com.college.qms.dto.ApiResponse;
import com.college.qms.dto.ServiceRequest;
import com.college.qms.entity.Service;
import com.college.qms.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    private void verifyAdmin(String role) {
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            throw new SecurityException("Access denied: Only ADMINISTRATOR can modify services!");
        }
    }

    // 1. GET all services (Public for all roles, optionally filter activeOnly=true)
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices(
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
        if (activeOnly) {
            return ResponseEntity.ok(serviceService.getActiveServices());
        }
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    // 2. GET service by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    // 3. POST create new service (ADMIN only)
    @PostMapping
    public ResponseEntity<Service> createService(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody ServiceRequest request) {
        verifyAdmin(role);
        Service created = serviceService.createService(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // 4. PUT update existing service (ADMIN only)
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody ServiceRequest request) {
        verifyAdmin(role);
        Service updated = serviceService.updateService(id, request);
        return ResponseEntity.ok(updated);
    }

    // 5. DELETE service (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteService(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        verifyAdmin(role);
        serviceService.deleteService(id);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "Service deleted successfully with ID: " + id));
    }
}
