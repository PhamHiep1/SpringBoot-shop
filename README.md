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

---
  
##  Testing APIs via Postman
### Core APIs
Below are the primary endpoints. The full Postman collection is available in the repository (or can be imported).
### 1. Authentication & User
### Register User
* **Method:** `POST`
* **URL:** `http://localhost:8080/api/v1/auth/register`
* **Request:**
```json
{
    "full_name":"pth",
    "email":"dd4321@gmail.com",
    "phone_number": "0999999999",
    "address": "ngo b",
    "password":"12345789",
    "retype_password":"12345789",
    "date_of_birth":"1990-01-01",
    "facebook_account_id":0,
    "google_account_id":0
}
```
* **Success Response:**
```json
{
    "message": "register successfully",
    "status": "201 CREATED",
    "data": {
        "id": 17,
        "full_name": "pth",
        "phone_number": "0999999999",
        "email":"dd4321@gmail.com",
        "address": "ngo b",
        "is_active": true,
        "date_of_birth": "1990-01-01T00:00:00.000Z",
        "facebook_account_id": 0,
        "google_account_id": 0,
        "role": {
            "id": 1,
            "name": "user"
        }
    }
}
```
### login User
* **Method:** `POST`
* **URL:** `http://localhost:8080/api/v1/auth/login`
* **Request:**
```json
{
    "phone_or_email":"dd4321@gmail.com",
    "password":"123456789"
}
```
* **Success Response:**
```json
{
    "message": "Login successfully",
    "status": "200 OK",
    "data": {
        "message": "Login successfully",
        "token": "eyJhbGciOiJIUzM4NCJ9.eyJwaG9uZU51bWJlciI6IjAxMTExMTExMTEiLCJ1c2VySWQiOjE2LCJzdWIiOiIwMTExMTExMTExIiwiaWF0IjoxNzc3ODY1MjEzLCJleHAiOjE3Nzc5NTE2MTN9.9WUYUln8G4-nytYLo0BR9OnatP4cH-H_bPFALfWc-wAo8WhTll1U3IVLm9Ar0YCO",
        "refresh_token": "93ab6557-5e05-41de-90ce-02a4f19e8ffb",
        "tokenType": "Bearer",
        "id": 16,
        "username": "0977777777",
        "roles": [
            "ROLE_USER"
        ]
    }
}
```
### get User details
* **Method:** `POST` (for security)
* **URL:** `http://localhost:8080/api/v1/users/details`
* **Authorization**: Bearer Token <your_token>
* **Success Response:**
```json
{
    "message": "User details retrieved successfully",
    "status": "200 OK",
    "data": {
        "id": 16,
        "full_name": "pth",
        "phone_number": "0111111111",
        "address": "ngo b",
        "is_active": true,
        "date_of_birth": "1989-12-31T17:00:00.000Z",
        "facebook_account_id": 0,
        "google_account_id": 0,
        "role": {
            "id": 1,
            "name": "user"
        }
    }
}
```
### 2. Product
### get products using paging
* **Method:** `GET`
* **URL:** `http://localhost:8080/api/v1/products?page=0&limit=10&keyword=Rem&categoryId=2`
* **Success Response:**
```json
{
    "products": [
        {
            "id": 2,
            "name": "Practical Copper Computer",
            "price": 29.4,
            "thumbnail": "eb9e275e-7c3a-4e3a-885a-80f4a154eecf phong-cach-thoi-trang-nam.jpg",
            "description": "Voluptatum rem est non dolores rerum.",
            "quantity": 36,
            "category_id": 2,
            "images": [
                {
                    "id": 7,
                    "imageURL": "eb9e275e-7c3a-4e3a-885a-80f4a154eecf wp12024615.jpg"
                },
                {
                    "id": 8,
                    "imageURL": "fb9cdc54-82ca-4658-95f8-c8d487a7bdce wp12024615.jpg"
                }
            ],
            "created_at": "2025-10-25T12:28:33",
            "updated_at": "2026-04-30T10:53:59"
        },
        {
            "id": 16,
            "name": "Ergonomic Plastic Shirt",
            "price": 3.6,
            "thumbnail": null,
            "description": "Quos consequatur vel pariatur quisquam repellat aspernatur rem.",
            "quantity": 50,
            "category_id": 2,
            "images": [{
                    "id": 9,
                    "imageURL": "eb9e275e-7c3a-4e3a-885a-80f4a154eecf wp12024613.jpg"
                },
                {
                    "id": 10,
                    "imageURL": "fb9cdc54-82ca-4658-95f8-c8d487a7bdce wp12024614.jpg"
                }],
            "created_at": "2025-10-25T12:28:34",
            "updated_at": "2025-10-25T12:28:34"
        }
    ],
    "totalPages": 144
}
```
### upload image for product
* **Method:** `POST`
* **URL:** `http://localhost:8080/api/v1/products/uploads/2`
*  **Body:** form-data (key-files)
```json
[
    {
        "id": 13,
        "imageURL": "2bb929d4-24f3-4b95-bfaf-71a721a0abbb wp3646106-men-fashion-wallpapers.jpg"
    },
    {
        "id": 14,
        "imageURL": "42fb4783-21de-4980-a823-77964340ea18 wp12024615.jpg"
    }
]
```


