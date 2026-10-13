package com.college.qms.dto;

import java.time.LocalDateTime;

public class TokenPositionResponse {

    private Long tokenId;
    private String myTokenNumber;
    private String currentlyServingToken;
    private int peopleAhead;
    private String queueStatus;
    private Long serviceId;
    private String serviceName;
    private String counterName;
    private LocalDateTime createdAt;

    public TokenPositionResponse() {}

    public TokenPositionResponse(Long tokenId, String myTokenNumber, String currentlyServingToken,
                                 int peopleAhead, String queueStatus, Long serviceId,
                                 String serviceName, String counterName, LocalDateTime createdAt) {
        this.tokenId = tokenId;
        this.myTokenNumber = myTokenNumber;
        this.currentlyServingToken = currentlyServingToken;
        this.peopleAhead = peopleAhead;
        this.queueStatus = queueStatus;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.counterName = counterName;
        this.createdAt = createdAt;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getMyTokenNumber() {
        return myTokenNumber;
    }

    public void setMyTokenNumber(String myTokenNumber) {
        this.myTokenNumber = myTokenNumber;
    }

    public String getCurrentlyServingToken() {
        return currentlyServingToken;
    }

    public void setCurrentlyServingToken(String currentlyServingToken) {
        this.currentlyServingToken = currentlyServingToken;
    }

    public int getPeopleAhead() {
        return peopleAhead;
    }

    public void setPeopleAhead(int peopleAhead) {
        this.peopleAhead = peopleAhead;
    }

    public String getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(String queueStatus) {
        this.queueStatus = queueStatus;
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

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
