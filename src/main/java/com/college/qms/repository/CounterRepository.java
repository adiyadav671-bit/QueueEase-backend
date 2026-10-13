package com.college.qms.repository;

import com.college.qms.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
    List<Counter> findByServiceId(Long serviceId);
    List<Counter> findByActiveTrue();
}
