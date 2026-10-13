package com.college.qms.dto;

public class CounterResponse {

    private Long id;
    private String name;
    private Long serviceId;
    private String serviceName;
    private boolean active;

    public CounterResponse() {}

    public CounterResponse(Long id, String name, Long serviceId, String serviceName, boolean active) {
        this.id = id;
        this.name = name;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
