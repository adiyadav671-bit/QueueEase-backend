package com.college.qms.controller;

import com.college.qms.repository.CounterRepository;
import com.college.qms.repository.ServiceRepository;
import com.college.qms.repository.TokenRepository;
import com.college.qms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Online Queue Management System Backend is working!");
        response.put("databaseConnected", true);

        // Database statistics from repositories
        Map<String, Long> dbStats = new HashMap<>();
        dbStats.put("users", userRepository.count());
        dbStats.put("services", serviceRepository.count());
        dbStats.put("counters", counterRepository.count());
        dbStats.put("tokens", tokenRepository.count());

        response.put("recordsInDatabase", dbStats);
        return response;
    }
}
