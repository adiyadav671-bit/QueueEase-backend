package com.college.qms.service;

import com.college.qms.dto.TokenResponse;
import com.college.qms.entity.Counter;
import com.college.qms.entity.Service;
import com.college.qms.entity.Token;
import com.college.qms.repository.CounterRepository;
import com.college.qms.repository.ServiceRepository;
import com.college.qms.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class StaffService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CounterRepository counterRepository;

    // 1. View waiting queue for a service
    public List<TokenResponse> getWaitingQueue(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

        List<Token> waitingTokens = tokenRepository.findByServiceIdAndStatusOrderByIdAsc(service.getId(), "WAITING");
        return waitingTokens.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // 2. Call next available token: WAITING -> SERVING
    public TokenResponse callNextToken(Long serviceId, Long counterId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

        List<Token> waitingTokens = tokenRepository.findByServiceIdAndStatusOrderByIdAsc(service.getId(), "WAITING");
        if (waitingTokens.isEmpty()) {
            throw new IllegalArgumentException("No waiting customers in queue for '" + service.getName() + "'.");
        }

        Token nextToken = waitingTokens.get(0);
        nextToken.setStatus("SERVING");

        if (counterId != null) {
            Counter counter = counterRepository.findById(counterId)
                    .orElseThrow(() -> new IllegalArgumentException("Counter not found with ID: " + counterId));
            nextToken.setCounter(counter);
        }

        Token saved = tokenRepository.save(nextToken);
        return mapToResponse(saved);
    }

    // 3. Complete service: SERVING -> COMPLETED
    public TokenResponse completeToken(Long tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found with ID: " + tokenId));

        if (!"SERVING".equalsIgnoreCase(token.getStatus())) {
            throw new IllegalArgumentException("Cannot complete token: Token is not in SERVING status. Current status is " + token.getStatus());
        }

        token.setStatus("COMPLETED");
        Token saved = tokenRepository.save(token);
        return mapToResponse(saved);
    }

    // 4. Skip a token: WAITING or SERVING -> SKIPPED
    public TokenResponse skipToken(Long tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found with ID: " + tokenId));

        if (!"WAITING".equalsIgnoreCase(token.getStatus()) && !"SERVING".equalsIgnoreCase(token.getStatus())) {
            throw new IllegalArgumentException("Cannot skip token: Only WAITING or SERVING tokens can be skipped. Current status is " + token.getStatus());
        }

        token.setStatus("SKIPPED");
        Token saved = tokenRepository.save(token);
        return mapToResponse(saved);
    }

    private TokenResponse mapToResponse(Token token) {
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
            queuePosition = 0;
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
