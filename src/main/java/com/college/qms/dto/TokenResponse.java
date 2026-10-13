package com.college.qms.dto;

import java.time.LocalDateTime;

public class TokenResponse {

    private Long id;
    private String tokenNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long serviceId;
    private String serviceName;
    private Long counterId;
    private String counterName;
    private String status;
    private LocalDateTime createdAt;
    private Integer queuePosition;

    public TokenResponse() {}

    public TokenResponse(Long id, String tokenNumber, Long customerId, String customerName, String customerEmail,
                         Long serviceId, String serviceName, Long counterId, String counterName,
                         String status, LocalDateTime createdAt, Integer queuePosition) {
        this.id = id;
        this.tokenNumber = tokenNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.counterId = counterId;
        this.counterName = counterName;
        this.status = status;
        this.createdAt = createdAt;
        this.queuePosition = queuePosition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }
}
