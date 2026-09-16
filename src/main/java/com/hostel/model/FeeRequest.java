package com.hostel.model;

import java.time.LocalDate;

public class FeeRequest {

    private double amount;
    private String status;
    private LocalDate paymentDate;
    private Long studentId;

    public FeeRequest() {
    }

    public FeeRequest(double amount, String status, LocalDate paymentDate, Long studentId) {
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.studentId = studentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
