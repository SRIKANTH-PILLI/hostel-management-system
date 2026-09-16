
package com.hostel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.model.Room;
import com.hostel.repository.RoomRepository;
import com.hostel.repository.StudentRepository;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private final StudentRepository studentRepository;

    public RoomController(RoomRepository roomRepository,
                          StudentRepository studentRepository) {
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
    }

    // Common room validation
    private String validateRoom(Room room) {

        // Validate room number
        if (room.getRoomNumber() == null ||
                room.getRoomNumber().trim().isEmpty()) {

            return "Room number is required";
        }

        // Validate capacity
        if (room.getCapacity() <= 0) {

            return "Room capacity must be greater than 0";
        }

        // Available beds cannot be negative
        if (room.getAvailableBeds() < 0) {

            return "Available beds cannot be negative";
        }

        // Available beds cannot be greater than capacity
        if (room.getAvailableBeds() > room.getCapacity()) {

            return "Available beds cannot be greater than room capacity";
        }

        return null;
    }

    // CREATE
    @PostMapping
    @Transactional
    public ResponseEntity<?> addRoom(@RequestBody Room room) {

        // Validate room
        String validationError = validateRoom(room);

        if (validationError != null) {
            return ResponseEntity
                    .badRequest()
                    .body(validationError);
        }

        return ResponseEntity.ok(
                roomRepository.save(room)
        );
    }

    // READ ALL
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {

        Room room = roomRepository
                .findById(id)
                .orElse(null);

        if (room == null) {
            return ResponseEntity
                    .status(404)
                    .body("Room not found with ID: " + id);
        }

        return ResponseEntity.ok(room);
    }

    // UPDATE
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateRoom(@PathVariable Long id,
                                        @RequestBody Room room) {

        // Validate room number and capacity
        if (room.getRoomNumber() == null ||
                room.getRoomNumber().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Room number is required");
        }

        if (room.getCapacity() <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Room capacity must be greater than 0");
        }

        // Find existing room
        Room existingRoom = roomRepository
                .findById(id)
                .orElse(null);

        if (existingRoom == null) {
            return ResponseEntity
                    .status(404)
                    .body("Room not found with ID: " + id);
        }

        // Count students currently assigned to this room
        long occupiedBeds =
                studentRepository.countByRoomId(id);

        // Capacity cannot be less than current occupancy
        if (room.getCapacity() < occupiedBeds) {

            return ResponseEntity
                    .badRequest()
                    .body("Room capacity cannot be less than occupied beds: "
                            + occupiedBeds);
        }

        // Update room number
        existingRoom.setRoomNumber(
                room.getRoomNumber()
        );

        // Update capacity
        existingRoom.setCapacity(
                room.getCapacity()
        );

        // Automatically calculate available beds
        int availableBeds =
                (int) (room.getCapacity() - occupiedBeds);

        existingRoom.setAvailableBeds(
                availableBeds
        );

        return ResponseEntity.ok(
                roomRepository.save(existingRoom)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {

        Room room = roomRepository
                .findById(id)
                .orElse(null);

        if (room == null) {
            return ResponseEntity
                    .status(404)
                    .body("Room not found with ID: " + id);
        }

        // Prevent deleting a room that has students
        long occupiedBeds =
                studentRepository.countByRoomId(id);

        if (occupiedBeds > 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Cannot delete room. "
                            + occupiedBeds
                            + " student(s) are currently assigned to this room");
        }

        roomRepository.delete(room);

        return ResponseEntity.ok(
                "Room deleted successfully"
        );
    }
}









