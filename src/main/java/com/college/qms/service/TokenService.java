package com.college.qms.service;

import com.college.qms.dto.ServiceQueueResponse;
import com.college.qms.dto.TokenPositionResponse;
import com.college.qms.dto.TokenRequest;
import com.college.qms.dto.TokenResponse;
import com.college.qms.entity.Service;
import com.college.qms.entity.Token;
import com.college.qms.entity.User;
import com.college.qms.repository.ServiceRepository;
import com.college.qms.repository.TokenRepository;
import com.college.qms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public TokenResponse generateToken(TokenRequest request) {
        if (request.getUserId() == null || request.getServiceId() == null) {
            throw new IllegalArgumentException("Customer ID and Service ID are required to generate a token.");
        }

        // 1. Validate Customer
        User customer = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + request.getUserId()));

        // 2. Validate Service
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + request.getServiceId()));

        if (!service.isActive()) {
            throw new IllegalArgumentException("Selected service '" + service.getName() + "' is currently closed or inactive.");
        }

        // 3. Prevent customer from taking multiple active tokens for the same service
        boolean hasActiveToken = tokenRepository.existsByUserIdAndServiceIdAndStatusIn(
                customer.getId(),
                service.getId(),
                Arrays.asList("WAITING", "SERVING")
        );

        if (hasActiveToken) {
            throw new IllegalArgumentException("You already have an active queue token for '" + service.getName() + "'. Please wait for your turn or cancel it.");
        }

        // 4. Generate simple sequential token number (e.g. 1, 2, 3...) per service
        long nextNumber = tokenRepository.countByServiceId(service.getId()) + 1;
        String tokenNumber = String.valueOf(nextNumber);

        // 5. Create and save new token
        Token token = new Token();
        token.setTokenNumber(tokenNumber);
        token.setUser(customer);
        token.setService(service);
        token.setCounter(null);
        token.setStatus("WAITING");

        Token savedToken = tokenRepository.save(token);

        return mapToResponse(savedToken);
    }

    public TokenResponse getTokenById(Long id) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Token not found with ID: " + id));
        return mapToResponse(token);
    }

    public List<TokenResponse> getTokensByUserId(Long userId) {
        return tokenRepository.findByUserIdOrderByIdDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TokenResponse> getAllTokens() {
        return tokenRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TokenPositionResponse getTokenPosition(Long id) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Token not found with ID: " + id));

        Long serviceId = token.getService().getId();

        // 1. Find currently serving token for this service
        List<Token> servingTokens = tokenRepository.findByServiceIdAndStatus(serviceId, "SERVING");
        String currentlyServing = "None";
        if (!servingTokens.isEmpty()) {
            currentlyServing = servingTokens.stream()
                    .map(t -> "Token " + t.getTokenNumber())
                    .collect(Collectors.joining(", "));
        }

        // 2. Calculate people ahead (number of WAITING tokens before this token)
        int peopleAhead = 0;
        if ("WAITING".equalsIgnoreCase(token.getStatus())) {
            List<Token> waitingTokens = tokenRepository.findByServiceIdAndStatusOrderByIdAsc(serviceId, "WAITING");
            int index = 0;
            for (Token wt : waitingTokens) {
                if (wt.getId().equals(token.getId())) {
                    peopleAhead = index;
                    break;
                }
                index++;
            }
        }

        String counterName = token.getCounter() != null ? token.getCounter().getName() : "Unassigned";

        return new TokenPositionResponse(
                token.getId(),
                token.getTokenNumber(),
                currentlyServing,
                peopleAhead,
                token.getStatus(),
                serviceId,
                token.getService().getName(),
                counterName,
                token.getCreatedAt()
        );
    }

    public ServiceQueueResponse getServiceQueue(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

        List<Token> servingTokens = tokenRepository.findByServiceIdAndStatus(serviceId, "SERVING");
        List<Token> waitingTokens = tokenRepository.findByServiceIdAndStatusOrderByIdAsc(serviceId, "WAITING");

        String currentlyServing = "None";
        if (!servingTokens.isEmpty()) {
            currentlyServing = servingTokens.stream()
                    .map(t -> "Token " + t.getTokenNumber())
                    .collect(Collectors.joining(", "));
        }

        List<TokenResponse> servingList = servingTokens.stream().map(this::mapToResponse).collect(Collectors.toList());
        List<TokenResponse> waitingList = waitingTokens.stream().map(this::mapToResponse).collect(Collectors.toList());

        return new ServiceQueueResponse(
                service.getId(),
                service.getName(),
                waitingList.size(),
                currentlyServing,
                servingList,
                waitingList
        );
    }

    public TokenResponse cancelToken(Long id, Long userId) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Token not found with ID: " + id));

        // 1. Ownership check: Customer cannot cancel another customer's token
        if (userId != null && !token.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied: You cannot cancel another customer's token!");
        }

        // 2. Status rules check
        String currentStatus = token.getStatus().toUpperCase();
        if ("COMPLETED".equals(currentStatus)) {
            throw new IllegalArgumentException("Cannot cancel token: A COMPLETED token cannot be cancelled.");
        }
        if ("SKIPPED".equals(currentStatus)) {
            throw new IllegalArgumentException("Cannot cancel token: A SKIPPED token cannot be cancelled.");
        }
        if ("CANCELLED".equals(currentStatus)) {
            throw new IllegalArgumentException("Cannot cancel token: This token is already cancelled.");
        }
        if (!"WAITING".equals(currentStatus)) {
            throw new IllegalArgumentException("Cannot cancel token: Only WAITING tokens can be cancelled. Current status is " + currentStatus);
        }

        token.setStatus("CANCELLED");
        Token updated = tokenRepository.save(token);
        return mapToResponse(updated);
    }

    private TokenResponse mapToResponse(Token token) {
        // Calculate queue position if token is currently WAITING
        Integer queuePosition = null;
        if ("WAITING".equalsIgnoreCase(token.getStatus())) {
            List<Token> waitingTokens = tokenRepository.findByServiceIdAndStatusOrderByIdAsc(
                    token.getService().getId(),
                    "WAITING"
            );
            int pos = 1;
            for (Token wt : waitingTokens) {
                if (wt.getId().equals(token.getId())) {
                    queuePosition = pos;
                    break;
                }
                pos++;
            }
        } else if ("SERVING".equalsIgnoreCase(token.getStatus())) {
            queuePosition = 0; // Currently at the counter
        }

        Long counterId = token.getCounter() != null ? token.getCounter().getId() : null;
        String counterName = token.getCounter() != null ? token.getCounter().getName() : "Unassigned";

        return new TokenResponse(
                token.getId(),
                token.getTokenNumber(),
                token.getUser().getId(),
                token.getUser().getName(),
                token.getUser().getEmail(),
                token.getService().getId(),
                token.getService().getName(),
                counterId,
                counterName,
                token.getStatus(),
                token.getCreatedAt(),
                queuePosition
        );
    }
}
