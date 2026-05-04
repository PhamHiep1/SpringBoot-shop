# E-Commerce ShopApp - Backend System

A comprehensive E-commerce backend system designed and developed to manage product operations, secure user authentication, and handle RESTful API workflows.
---

## 🛠️ Tech Stack

### **Backend:**
* **Language:** Java 17
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security 6.x, JSON Web Token (JWT), Refresh Token Rotation
* **Database:** MySQL
* **Data Access:** Spring Data JPA, Hibernate, DTO Validation
* **Error Handling:** Global Exception Handling (`@RestControllerAdvice`)
* **Testing:** Postman

### **Frontend Integration:**
* **Framework:** Angular 

---

## 👥 Team & Role
* **Team Size:** 2 members
* **My Role:** **Backend Developer** – Designed and implemented all backend business logic, security layers (JWT/Refresh Token), global exception handling, and REST APIs.

---

## 🌟 Key Features

* **Authentication & Authorization:**
  * User registration and login mechanisms with JWT issuance.
  * Implemented **Refresh Token Rotation** to secure sessions against replay attacks.
  * Stateless security using `OncePerRequestFilter`.
* **Centralized Exception Handling:**
  * Custom handling for validation, data not found, and security violations, returning standardized JSON responses.
* **E-Commerce Core Modules:**
  * Product management with pagination, filtering, and detail retrieval.
  * Order creation, update, and soft-delete capabilities.
  * Multi-part image uploading and processing.
  
