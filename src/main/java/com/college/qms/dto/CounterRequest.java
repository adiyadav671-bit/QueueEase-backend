package com.college.qms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CounterRequest {

    @NotBlank(message = "Counter name is required")
    private String name;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    private Boolean active = true;

    public CounterRequest() {}

    public CounterRequest(String name, Long serviceId, Boolean active) {
        this.name = name;
        this.serviceId = serviceId;
        this.active = active != null ? active : true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
