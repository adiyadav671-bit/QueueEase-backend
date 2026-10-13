package com.college.qms.service;

import com.college.qms.dto.ServiceRequest;
import com.college.qms.entity.Service;
import com.college.qms.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<Service> getActiveServices() {
        return serviceRepository.findByActiveTrue();
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + id));
    }

    public Service createService(ServiceRequest request) {
        Service service = new Service();
        service.setName(request.getName().trim());
        service.setDescription(request.getDescription());
        service.setActive(request.getActive() != null ? request.getActive() : true);
        return serviceRepository.save(service);
    }

    public Service updateService(Long id, ServiceRequest request) {
        Service service = getServiceById(id);
        service.setName(request.getName().trim());
        service.setDescription(request.getDescription());
        if (request.getActive() != null) {
            service.setActive(request.getActive());
        }
        return serviceRepository.save(service);
    }

    public void deleteService(Long id) {
        Service service = getServiceById(id);
        serviceRepository.delete(service);
    }
}
