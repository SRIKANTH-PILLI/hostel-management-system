
package com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hostel.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    long countByRoomId(Long roomId);
}


