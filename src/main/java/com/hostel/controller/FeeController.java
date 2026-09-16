package com.hostel.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.model.Fee;
import com.hostel.model.FeeRequest;
import com.hostel.model.Student;
import com.hostel.repository.FeeRepository;
import com.hostel.repository.StudentRepository;

@RestController
@RequestMapping("/fees")
public class FeeController {

    private final FeeRepository feeRepository;
    private final StudentRepository studentRepository;

    public FeeController(FeeRepository feeRepository,
                          StudentRepository studentRepository) {
        this.feeRepository = feeRepository;
        this.studentRepository = studentRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> addFee(@RequestBody FeeRequest request) {

        // Validate amount
        if (request.getAmount() <= 0) {
            return ResponseEntity
                    .badRequest()
                    .body("Fee amount must be greater than 0");
        }

        // Validate status
        if (request.getStatus() == null ||
                (!request.getStatus().equalsIgnoreCase("PAID")
                && !request.getStatus().equalsIgnoreCase("PENDING"))) {

            return ResponseEntity
                    .badRequest()
                    .body("Fee status must be PAID or PENDING");
        }

        // Validate payment date
        if (request.getPaymentDate() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Payment date is required");
        }

        if (request.getPaymentDate().isAfter(LocalDate.now())) {
            return ResponseEntity
                    .badRequest()
                    .body("Payment date cannot be in the future");
        }

        // Validate student ID
        if (request.getStudentId() == null ||
                request.getStudentId() <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Valid student ID is required");
        }

        // Check whether student exists
        Student student = studentRepository
                .findById(request.getStudentId())
                .orElse(null);

        if (student == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found with ID: "
                            + request.getStudentId());
        }

        Fee fee = new Fee();

        fee.setAmount(request.getAmount());
        fee.setStatus(request.getStatus().toUpperCase());
        fee.setPaymentDate(request.getPaymentDate());
        fee.setStudent(student);

        Fee savedFee = feeRepository.save(fee);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedFee);
    }

    // READ ALL
    @GetMapping
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeeById(@PathVariable Long id) {

        Fee fee = feeRepository
                .findById(id)
                .orElse(null);

        if (fee == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Fee not found with ID: " + id);
        }

        return ResponseEntity.ok(fee);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFee(@PathVariable Long id) {

        Fee fee = feeRepository
                .findById(id)
                .orElse(null);

        if (fee == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Fee not found with ID: " + id);
        }

        feeRepository.delete(fee);

        return ResponseEntity.ok("Fee deleted successfully");
    }
}








