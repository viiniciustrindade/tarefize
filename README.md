# 📝 To-Do List API

> **API available at:** https://todolist-api-8hia.onrender.com/swagger-ui/index.html

A robust REST API for task management built with **Spring Boot 3**.  
This project was developed following best development practices, focusing on **security, scalability, and clean architecture**.

The system allows users to manage their tasks in an organized and secure way and introduces an **event-driven approach** using **Kafka-compatible messaging with Redpanda** to enable asynchronous processing and microservice communication.

---

# 🚀 Technologies Used

* **Java 21**
* **Spring Boot 3**
* **Spring Security** (JWT Authentication)
* **Spring Data JPA** (Data Persistence)
* **PostgreSQL** (Relational Database)
* **Apache Kafka API**
* **Redpanda** (Kafka-compatible streaming platform)
* **Bean Validation** (Hibernate Validator)
* **Lombok** (Boilerplate code reduction)
* **Swagger / OpenAPI** (Interactive API documentation)
* **JUnit & MockMvc** (Testing)

---

# 📌 Main Features

### 🔐 Authentication & Security

* **User Registration and Login**
* Password encryption using **BCrypt**
* **JWT-based authentication**
* Protected endpoints using **Spring Security**

### ✅ Task Management

Full **CRUD operations** for tasks:

* Create tasks
* List user tasks
* Update tasks
* Delete tasks

### 🔒 Data Isolation

Each user has **exclusive access to their own tasks**, ensuring data privacy and security.

### 📊 Domain Validation

Tasks include a **priority system (1–5)** validated at the input layer using **Bean Validation**.

### ⚠️ Global Error Handling

Centralized exception management using **GlobalExceptionHandler**, returning standardized API responses for:

* Business rule violations
* Validation errors
* Authentication failures

---

# 🧠 Event-Driven Architecture (Kafka / Redpanda)

This project introduces an **asynchronous event-driven pattern** to support future microservices.

When a task is created:

1. The API processes and persists the task in the database.
2. An **event message is published to a Kafka topic**.
3. The message is sent to **Redpanda**, a high-performance Kafka-compatible streaming platform.
4. External services (such as a **Notification Service**) can subscribe to this topic and react to the event.

### Example Event Flow

```

User → Create Task
↓
Spring Boot API
↓
Persist in PostgreSQL
↓
Publish Event
↓
Kafka Topic (Redpanda)
↓
Notification Service (Future Consumer)

````

This architecture enables:

* **Loose coupling between services**
* **Scalability for future microservices**
* **Asynchronous processing**
* **Event-based integrations**

---

# 🏗️ Architecture and Best Practices

The project follows **Layered Architecture** and **SOLID principles**, ensuring maintainability and testability.

### Layers

**Controller**

Handles HTTP requests and validates incoming data using `@Valid`.

**Service**

Contains business logic and orchestrates persistence and messaging operations.

**Repository**

Interface responsible for communication with the database using **Spring Data JPA**.

**DTO (Data Transfer Objects)**

Records used to safely transfer data, preventing direct exposure of entity classes.

---

# 📖 How to Run the Project

## 1️⃣ Clone the repository

```bash
git clone https://github.com/viiniciustrindade/todolist-api.git
````

---

## 2️⃣ Configure the Database

Update PostgreSQL credentials in:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

---

## 3️⃣ Configure Kafka / Redpanda (Optional for Local Testing)

To run messaging locally you can use **Redpanda via Docker**:

```bash
docker run -d --name redpanda \
  -p 9092:9092 \
  redpandadata/redpanda
```

---

## 4️⃣ Run the Application

```bash
./mvnw spring-boot:run
```

---

## 5️⃣ Access the API Documentation

Open in your browser:

```
http://localhost:8081/swagger-ui.html
```

Swagger allows you to **interactively test all endpoints**.

---

# 🧪 Quality and Testing

The project includes an automated test suite to ensure the reliability of business rules and endpoint behavior.

### Integration Tests

Implemented with **MockMvc** to validate the complete request lifecycle:

```
Controller → Service → Repository
```

Tests cover:

* JWT authentication
* Endpoint access control
* Request validation
* Exception handling
* Data persistence

### Test Profiles

Uses:

```java
@ActiveProfiles("test")
```

to isolate the testing environment from development and production environments.

### Error Validation

Specific tests ensure the `GlobalExceptionHandler` returns correct:

* HTTP status codes
* Error messages
* Validation responses

---

# ☁️ Deployment and CI/CD

The project is production-ready and integrated with modern automation tools.

### CI/CD (GitHub Actions)

Pipeline configured to:

* Build the application
* Run all automated tests
* Validate pull requests
* Prevent merging if tests fail

### Hosting

The API is deployed on **Render** and connected to a managed **PostgreSQL database**.

### Production Security

Sensitive information is managed using **environment variables**:

* JWT Secret
* Database credentials
* Kafka configuration

This follows **industry security best practices**.

---

# 📌 Future Improvements

Planned improvements for the project include:

* **Notification Microservice**
* **Kafka Consumer for task events**
* **Email or Push Notifications**
* **Task deadlines with scheduled jobs**
* **Observability with metrics and logs**

---

# 👨‍💻 Author

Developed by **Vinicius Trindade**

Backend Developer focused on:

* **Java & Spring Boot**
* **Microservices Architecture**
* **Event-driven systems**
* **Secure REST APIs**
---
