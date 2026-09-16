package com.hostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hostel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
