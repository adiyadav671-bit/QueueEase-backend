package com.college.qms.controller;

import com.college.qms.dto.ServiceQueueResponse;
import com.college.qms.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queues")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class QueueController {

    @Autowired
    private TokenService tokenService;

    // GET /api/queues/{serviceId} - View current queue for a specific service
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceQueueResponse> getServiceQueue(@PathVariable Long serviceId) {
        ServiceQueueResponse response = tokenService.getServiceQueue(serviceId);
        return ResponseEntity.ok(response);
    }
}
