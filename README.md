# 📝 To-Do List API

> **API available at:** https://todolist-api-8hia.onrender.com/swagger-ui/index.html

A robust REST API for task management built with **Spring Boot 3**.  
This project was developed following best development practices, focusing on security with JWT and a clean architecture approach, allowing users to manage their tasks in an organized and secure way.

## 🚀 Technologies Used

* **Java 21**
* **Spring Boot 3**
* **Spring Security** (JWT Authentication)
* **Spring Data JPA** (Data Persistence)
* **PostgreSQL** (Relational Database)
* **Bean Validation** (Data validation with Hibernate Validator)
* **Lombok** (Boilerplate code reduction)
* **Swagger/OpenAPI** (Interactive API documentation)

## 📌 Main Features

* **User Authentication:** User registration and login system with encrypted passwords (BCrypt).
* **Task Management (CRUD):** Create, list, update, and delete tasks.
* **Data Isolation:** Each user has exclusive access only to their own tasks.
* **Domain Validation:** Priority system (range from 1 to 5) validated at the input layer.
* **Error Handling:** Global handler to capture business exceptions and validation errors, returning standardized responses.

## 🏗️ Architecture and Best Practices

The project follows **Layered Architecture** and **SOLID principles**, ensuring maintainability and testability.

* **Controller:** Handles routes and validates incoming data using `@Valid`.
* **Service:** Centralizes business rules and orchestrates persistence operations.
* **Repository:** Interface responsible for communication with the database.
* **DTO (Data Transfer Objects):** Records used to safely transfer data, preventing direct exposure of entity classes.

## 📖 How to Run the Project

1. **Clone the repository:**

```bash
git clone https://github.com/viiniciustrindade/todolist-api.git
````

2. **Configure the Database**

Update the PostgreSQL credentials in:

```
src/main/resources/application.properties
```

3. **Run the application**

```bash
./mvnw spring-boot:run
```

4. **Access the Documentation**

Open your browser at:

```
http://localhost:8081/swagger-ui.html
```

to test the API endpoints using Swagger.

## 🧪 Quality and Testing

The project includes an automated test suite to ensure the reliability of business rules and the integrity of the endpoints.

* **Integration Tests:** Implemented with `MockMvc` to validate the complete request lifecycle (Controller → Service → Repository), ensuring JWT authentication and access permissions work correctly.
* **Test Profiles:** Uses `@ActiveProfiles("test")` to isolate the testing environment from the development environment.
* **Error Validation:** Specific tests ensure that the `GlobalExceptionHandler` returns the correct HTTP status codes and messages in exception scenarios.

## ☁️ Deployment and CI/CD

The application is production-ready and integrated with modern automation tools.

* **CI/CD (GitHub Actions):** Pipeline configured to automatically run the entire test suite on every `push` or `pull request`. Deployment is only allowed if all tests pass.
* **Hosting (Render):** The API is hosted on Render and connected to a managed PostgreSQL database.
* **Production Security:** Strict use of environment variables to protect sensitive data (JWT Secret and Database Credentials), following industry security best practices.
