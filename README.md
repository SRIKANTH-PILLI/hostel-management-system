# Hostel Management System

A backend REST API application developed using Java and Spring Boot to manage hostel rooms, students, and fee records.

## Features

* Manage hostel rooms
* Add, view, update, and delete students
* Assign students to rooms
* Transfer students between rooms
* Automatically update available beds
* Manage student fee records
* Prevent deletion of students with existing fee records
* Prevent deletion of rooms with assigned students
* Validate student, room, and fee information
* PostgreSQL database integration
* RESTful API architecture

## Technologies Used

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* REST API
* Eclipse IDE
* Postman

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
```

## Database

The application uses PostgreSQL as the relational database.

### Main Tables

* `rooms`
* `students`
* `fees`

### Relationships

* One room can have multiple students.
* Each student is assigned to one room.
* One student can have multiple fee records.
* A fee record belongs to a student.

## REST API Endpoints

### Room APIs

| Method | Endpoint      | Description    |
| ------ | ------------- | -------------- |
| POST   | `/rooms`      | Add a new room |
| GET    | `/rooms`      | Get all rooms  |
| GET    | `/rooms/{id}` | Get room by ID |
| PUT    | `/rooms/{id}` | Update room    |
| DELETE | `/rooms/{id}` | Delete room    |

### Student APIs

| Method | Endpoint         | Description       |
| ------ | ---------------- | ----------------- |
| POST   | `/students`      | Add a new student |
| GET    | `/students`      | Get all students  |
| GET    | `/students/{id}` | Get student by ID |
| PUT    | `/students/{id}` | Update student    |
| DELETE | `/students/{id}` | Delete student    |

### Fee APIs

| Method | Endpoint     | Description         |
| ------ | ------------ | ------------------- |
| POST   | `/fees`      | Add a fee record    |
| GET    | `/fees`      | Get all fee records |
| GET    | `/fees/{id}` | Get fee by ID       |
| DELETE | `/fees/{id}` | Delete a fee record |

## Sample API Requests

### Add Room

```json
{
    "roomNumber": "101",
    "capacity": 4,
    "availableBeds": 4
}
```

### Add Student

```json
{
    "name": "Rahul",
    "age": 21,
    "gender": "Male",
    "phone": "9876543211",
    "room": {
        "id": 1
    }
}
```

### Add Fee

```json
{
    "amount": 5000,
    "status": "PAID",
    "paymentDate": "2026-09-16",
    "studentId": 1
}
```

## Validation

The application performs validation for:

* Required student fields
* Student age
* Indian mobile phone number
* Room number
* Room capacity
* Available beds
* Fee amount
* Fee status
* Payment date
* Student existence
* Room existence
* Room availability

The application also prevents invalid operations such as:

* Assigning a student to a full room
* Deleting a room that has assigned students
* Deleting a student that has existing fee records

## How to Run

### Prerequisites

* Java 21
* PostgreSQL
* Maven
* Eclipse IDE or another Java IDE
* Postman

### Steps

1. Clone this repository.
2. Open the project in Eclipse or another Java IDE.
3. Create a PostgreSQL database.
4. Configure the database connection in your local `application.properties`.
5. Make sure PostgreSQL is running.
6. Run `HostelManagementApplication.java`.
7. The Spring Boot application starts on:

```text
http://localhost:8080
```

8. Use Postman to test the REST APIs.

### Database Configuration

Do not upload your real database password to GitHub.

Configure your local database credentials in:

```text
src/main/resources/application.properties
```

## API Testing

The REST APIs were tested using Postman.

Example API operations tested:

* Create and retrieve rooms
* Create and retrieve students
* Assign students to rooms
* Transfer students between rooms
* Update available beds automatically
* Create and retrieve fee records
* Validate invalid input
* Prevent deletion when related records exist

## Future Enhancements

* User authentication and authorization
* Admin dashboard
* Student search and filtering
* Fee payment history
* Room availability dashboard
* Frontend using React or Angular
* Cloud deployment
* API documentation using Swagger/OpenAPI

## Author

**Srikanth**

B.Tech – Electrical and Electronics Engineering

GitHub: **SRIKANTH-PILLI**
