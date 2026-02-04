# Booking System 

## Overview

This project is a **backend-only booking system** built using **Spring Boot** that ensures **data consistency and correctness under high concurrency**.

Multiple users can attempt to book the same slot concurrently, but the system guarantees that **a slot can be booked only once** by using **database-level locking and transactional guarantees**. No in-memory locks or synchronized blocks are used.

---

## Key Features

- Role-based access control (USER, ADMIN)
- Slot creation and management
- Slot booking and cancellation
- Race condition protection
- Database-level locking
- Transactional consistency
- H2 in-memory database

---

## Technology Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security 6
- H2 Database
- Maven
- JUnit 5

---

## Roles and Permissions

### USER
- View slots
- Book a slot
- Cancel own booking

### ADMIN
- Create slots
- View slots
- Cancel any booking

---

## Data Model

### Slot

- **id** (Long): Unique identifier for the slot.
- **startTime** (LocalDateTime): The start time of the slot.
- **endTime** (LocalDateTime): The end time of the slot.
- **status** (Enum): The current status of the slot. Possible values:
  - `AVAILABLE`
  - `BOOKED`

### Booking

- **id** (Long): Unique identifier for the booking.
- **slot_id** (Long): The ID of the associated slot.
- **userId** (Long): The ID of the user who booked the slot.
- **status** (Enum): The current status of the booking. Possible values:
  - `ACTIVE`
  - `CANCELLED`
- **createdAt** (LocalDateTime): Timestamp when the booking was created.


---

## Concurrency Handling (Core Requirement)

### Problem

When multiple users try to book the same slot at the same time, race conditions can cause **double booking** if not handled correctly.

---

### Solution: Pessimistic Database Locking

The system uses **pessimistic locking** at the database level:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
SELECT s FROM Slot s WHERE s.id = :id
```
---

## How It Works

- The first booking request acquires a database lock on the slot row.
- Concurrent booking requests wait until the lock is released.
- The first transaction commits and marks the slot as `BOOKED`.
- Waiting transactions read the updated slot state and fail gracefully.

--- 

### Result

- ✔ Only one booking succeeds  
- ✔ No double booking  
- ✔ Safe under high concurrency  
- ✔ Correctness preserved across application restarts  

---

## Transaction Management

The booking operation is executed inside a **single transactional boundary**:

```java
@Transactional
public Booking bookSlot(...)
```
---

## Guarantees

- Slot status and booking record are updated atomically
- Partial updates cannot occur
- Automatic rollback on failure
- Slot and booking tables remain consistent

---

## Database Safety Measures

Multiple layers of safety are applied:

- Pessimistic row-level locking
- Unique constraint on `booking.slot_id`
- Explicit transactional boundaries
- Automatic rollback on exceptions

These measures guarantee **strong consistency**, even under extreme concurrency.

---

## API Endpoints

### Slot APIs

- **Create a Slot**  
  - **Endpoint:** `/slots`  
  - **Method:** POST  
  - **Role:** ADMIN  
  - **Description:** Allows the admin to create a new bookable slot.

- **Get Slots**  
  - **Endpoint:** `/slots`  
  - **Method:** GET  
  - **Role:** USER, ADMIN  
  - **Description:** Retrieves a list of all slots, both available and booked.

---

### Booking APIs

- **Book a Slot**  
  - **Endpoint:** `/bookings`  
  - **Method:** POST  
  - **Role:** USER  
  - **Description:** Allows a user to attempt booking a slot.

- **Cancel Own Booking**  
  - **Endpoint:** `/bookings/{id}/cancel`  
  - **Method:** POST  
  - **Role:** USER  
  - **Description:** Allows a user to cancel their own booking.

- **Admin Cancel Any Booking**  
  - **Endpoint:** `/admin/bookings/{id}/cancel`  
  - **Method:** POST  
  - **Role:** ADMIN  
  - **Description:** Allows an admin to cancel any booking in the system.


---

## Security Configuration

- Spring Security 6 (non-deprecated APIs)
- HTTP Basic Authentication (used for assignment simplicity)
- Method-level authorization using `@PreAuthorize`
- H2 console enabled for development purposes

---

## Example Concurrent Booking Scenario

### Scenario

- Slot ID: 1
- Two users attempt to book the same slot simultaneously

### Outcome

- One request succeeds
- One request fails with a conflict response
- Slot is booked exactly once

Concurrency behavior is validated using **multi-threaded unit tests**.

---

## How to Run the Application

```bash
mvn clean spring-boot:run
```

---

## H2 Console Access

- **URL:** http://localhost:8080/h2-console  
- **JDBC URL:** jdbc:h2:mem:bookingdb  
- **Username:** sa  
- **Password:** (empty)

---

## Why Pessimistic Locking?

Pessimistic locking was chosen because:

- Slot booking is a high-contention operation
- Immediate consistency is required
- No retry logic is needed
- Commonly used in real-world booking systems (tickets, seats, appointments)

---

## Conclusion

This project demonstrates:

- Correct handling of race conditions
- Database-level concurrency control
- Strong transactional consistency
- Production-ready backend architecture
- The system is safe, consistent, restart-proof, and concurrency-resilient




