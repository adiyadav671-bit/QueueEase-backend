package com.college.qms.service;

import com.college.qms.dto.CounterRequest;
import com.college.qms.dto.CounterResponse;
import com.college.qms.entity.Counter;
import com.college.qms.entity.Service;
import com.college.qms.repository.CounterRepository;
import com.college.qms.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class CounterService {

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<CounterResponse> getAllCounters() {
        return counterRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CounterResponse getCounterById(Long id) {
        Counter counter = counterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Counter not found with ID: " + id));
        return mapToResponse(counter);
    }

    public CounterResponse createCounter(CounterRequest request) {
        if (request.getServiceId() == null) {
            throw new IllegalArgumentException("Service ID is required to create a counter.");
        }
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot create counter: Service not found with ID: " + request.getServiceId()));

        Counter counter = new Counter();
        counter.setName(request.getName().trim());
        counter.setService(service);
        counter.setActive(request.getActive() != null ? request.getActive() : true);

        Counter saved = counterRepository.save(counter);
        return mapToResponse(saved);
    }

    public CounterResponse updateCounter(Long id, CounterRequest request) {
        if (request.getServiceId() == null) {
            throw new IllegalArgumentException("Service ID is required to update a counter.");
        }
        Counter counter = counterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Counter not found with ID: " + id));

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot update counter: Service not found with ID: " + request.getServiceId()));

        counter.setName(request.getName().trim());
        counter.setService(service);
        if (request.getActive() != null) {
            counter.setActive(request.getActive());
        }

        Counter updated = counterRepository.save(counter);
        return mapToResponse(updated);
    }

    public void deleteCounter(Long id) {
        Counter counter = counterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Counter not found with ID: " + id));
        counterRepository.delete(counter);
    }

    private CounterResponse mapToResponse(Counter counter) {
        Long serviceId = counter.getService() != null ? counter.getService().getId() : null;
        String serviceName = counter.getService() != null ? counter.getService().getName() : "None";
        return new CounterResponse(
                counter.getId(),
                counter.getName(),
                serviceId,
                serviceName,
                counter.isActive()
        );
    }
}
