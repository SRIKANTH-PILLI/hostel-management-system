# Hostel Management System

A backend web application developed using Java and Spring Boot to manage hostel rooms, students, and fee records.

## Features

- Manage hostel rooms
- Add, view, update, and delete students
- Assign students to rooms
- Transfer students between rooms
- Automatically update available beds
- Manage student fee records
- Prevent deletion of students with existing fee records
- Prevent deletion of rooms with assigned students
- Input validation for student, room, and fee data
- PostgreSQL database integration

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- REST API
- Eclipse IDE
- Postman

## Project Structure

```text
src/main/java/com/hostel
│
├── controller
│   ├── FeeController.java
│   ├── RoomController.java
│   └── StudentController.java
│
├── model
│   ├── Fee.java
│   ├── FeeRequest.java
│   ├── Room.java
│   └── Student.java
│
├── repository
│   ├── FeeRepository.java
│   ├── RoomRepository.java
│   └── StudentRepository.java
│
└── HostelManagementApplication.java