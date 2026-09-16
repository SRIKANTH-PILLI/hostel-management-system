package com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hostel.model.Fee;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    long countByStudentId(Long studentId);
}
