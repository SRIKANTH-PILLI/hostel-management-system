package com.hostel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.hostel.model.Student;
import com.hostel.repository.FeeRepository;
import com.hostel.repository.RoomRepository;
import com.hostel.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final FeeRepository feeRepository;

    public StudentController(StudentRepository studentRepository,
                             RoomRepository roomRepository,
                             FeeRepository feeRepository) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.feeRepository = feeRepository;
    }

    // Common student validation
    private String validateStudent(Student student) {

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Name is required";
        }

        if (student.getAge() == null ||
                student.getAge() <= 0 ||
                student.getAge() >= 100) {
            return "Age must be between 1 and 99";
        }

        if (student.getGender() == null ||
                student.getGender().trim().isEmpty()) {
            return "Gender is required";
        }

        if (student.getPhone() == null ||
                !student.getPhone().matches("[6-9][0-9]{9}")) {
            return "Phone number must be a valid 10-digit Indian mobile number";
        }

        return null;
    }

    // Check whether room has an available bed
    private boolean hasAvailableBed(Room room) {
        return room != null && room.getAvailableBeds() > 0;
    }

    // CREATE
    @PostMapping
    @Transactional
    public ResponseEntity<?> addStudent(@RequestBody Student student) {

        String validationError = validateStudent(student);

        if (validationError != null) {
            return ResponseEntity
                    .badRequest()
                    .body(validationError);
        }

        if (student.getRoom() != null &&
                student.getRoom().getId() != null) {

            Long roomId = student.getRoom().getId();

            Room room = roomRepository
                    .findById(roomId)
                    .orElse(null);

            if (room == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Room not found with ID: " + roomId);
            }

            if (!hasAvailableBed(room)) {
                return ResponseEntity
                        .badRequest()
                        .body("Room is full. No beds available");
            }

            student.setRoom(room);

            room.setAvailableBeds(
                    room.getAvailableBeds() - 1
            );

            roomRepository.save(room);
        }

        Student savedStudent = studentRepository.save(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    // READ ALL
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {

        Student student = studentRepository
                .findById(id)
                .orElse(null);

        if (student == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found with ID: " + id);
        }

        return ResponseEntity.ok(student);
    }

    // UPDATE
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateStudent(@PathVariable Long id,
                                           @RequestBody Student student) {

        String validationError = validateStudent(student);

        if (validationError != null) {
            return ResponseEntity
                    .badRequest()
                    .body(validationError);
        }

        Student existingStudent = studentRepository
                .findById(id)
                .orElse(null);

        if (existingStudent == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found with ID: " + id);
        }

        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        existingStudent.setGender(student.getGender());
        existingStudent.setPhone(student.getPhone());

        if (student.getRoom() != null &&
                student.getRoom().getId() != null) {

            Long newRoomId = student.getRoom().getId();

            Long oldRoomId = existingStudent.getRoom() != null
                    ? existingStudent.getRoom().getId()
                    : null;

            if (oldRoomId == null || !oldRoomId.equals(newRoomId)) {

                Room newRoom = roomRepository
                        .findById(newRoomId)
                        .orElse(null);

                if (newRoom == null) {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("Room not found with ID: " + newRoomId);
                }

                if (!hasAvailableBed(newRoom)) {
                    return ResponseEntity
                            .badRequest()
                            .body("Room is full. No beds available");
                }

                // Free bed in old room
                if (existingStudent.getRoom() != null) {

                    Room oldRoom = existingStudent.getRoom();

                    oldRoom.setAvailableBeds(
                            oldRoom.getAvailableBeds() + 1
                    );

                    roomRepository.save(oldRoom);
                }

                // Occupy bed in new room
                newRoom.setAvailableBeds(
                        newRoom.getAvailableBeds() - 1
                );

                roomRepository.save(newRoom);

                existingStudent.setRoom(newRoom);
            }
        }

        Student updatedStudent = studentRepository.save(existingStudent);

        return ResponseEntity.ok(updatedStudent);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {

        Student student = studentRepository
                .findById(id)
                .orElse(null);

        if (student == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found with ID: " + id);
        }

        // Check whether student has fee records
        long feeCount = feeRepository.countByStudentId(id);

        if (feeCount > 0) {
            return ResponseEntity
                    .badRequest()
                    .body("Cannot delete student. "
                            + feeCount
                            + " fee record(s) exist for this student. "
                            + "Delete the fee records first.");
        }

        // Free the room bed
        if (student.getRoom() != null) {

            Room room = student.getRoom();

            room.setAvailableBeds(
                    room.getAvailableBeds() + 1
            );

            roomRepository.save(room);
        }

        studentRepository.delete(student);

        return ResponseEntity.ok(
                "Student deleted successfully"
        );
    }
}









