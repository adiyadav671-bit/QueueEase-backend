package com.college.qms.repository;

import com.college.qms.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByStatus(String status);
    List<Token> findByUserId(Long userId);
    List<Token> findByUserIdOrderByIdDesc(Long userId);
    List<Token> findByServiceId(Long serviceId);
    List<Token> findByServiceIdAndStatus(Long serviceId, String status);
    List<Token> findByServiceIdAndStatusOrderByIdAsc(Long serviceId, String status);
    long countByServiceId(Long serviceId);
    boolean existsByUserIdAndServiceIdAndStatusIn(Long userId, Long serviceId, List<String> statuses);
}
