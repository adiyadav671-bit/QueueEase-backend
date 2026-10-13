package com.college.qms.dto;

import jakarta.validation.constraints.NotNull;

public class TokenRequest {

    @NotNull(message = "Customer ID is required")
    private Long userId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    public TokenRequest() {}

    public TokenRequest(Long userId, Long serviceId) {
        this.userId = userId;
        this.serviceId = serviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
