package com.college.qms.dto;

import java.util.List;

public class ServiceQueueResponse {

    private Long serviceId;
    private String serviceName;
    private int totalWaiting;
    private String currentlyServingTokens;
    private List<TokenResponse> servingList;
    private List<TokenResponse> waitingList;

    public ServiceQueueResponse() {}

    public ServiceQueueResponse(Long serviceId, String serviceName, int totalWaiting,
                                String currentlyServingTokens, List<TokenResponse> servingList,
                                List<TokenResponse> waitingList) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.totalWaiting = totalWaiting;
        this.currentlyServingTokens = currentlyServingTokens;
        this.servingList = servingList;
        this.waitingList = waitingList;
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

    public int getTotalWaiting() {
        return totalWaiting;
    }

    public void setTotalWaiting(int totalWaiting) {
        this.totalWaiting = totalWaiting;
    }

    public String getCurrentlyServingTokens() {
        return currentlyServingTokens;
    }

    public void setCurrentlyServingTokens(String currentlyServingTokens) {
        this.currentlyServingTokens = currentlyServingTokens;
    }

    public List<TokenResponse> getServingList() {
        return servingList;
    }

    public void setServingList(List<TokenResponse> servingList) {
        this.servingList = servingList;
    }

    public List<TokenResponse> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<TokenResponse> waitingList) {
        this.waitingList = waitingList;
    }
}
