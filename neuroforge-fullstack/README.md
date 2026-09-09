# NeuroForge Enterprise – Full Stack Project

A ready-to-run academic/demo SDLC management project with:
- Frontend: HTML, CSS, JavaScript
- Backend: Java 17 + Spring Boot + Spring Data JPA/Hibernate
- Database: MySQL
- REST API architecture
- CRUD for Users, Projects, Sprints, Tasks and Bug Reports

## Setup
### 1. Database
Create the database:
```sql
CREATE DATABASE neuroforge;
```
Edit `backend/src/main/resources/application.properties` and set your MySQL password.

### 2. Backend
Open `backend` in IntelliJ/Eclipse and run `com.neuroforge.NeuroForgeApplication`, or from the backend folder:
```bash
mvn spring-boot:run
```
Backend: `http://localhost:8080`

### 3. Frontend
Open `frontend/index.html`. If needed:
```bash
cd frontend
python -m http.server 5500
```
Then open `http://localhost:5500`.

## Architecture
Frontend → REST API → Spring Boot Controller → JPA/Hibernate → MySQL

## Important
This is an academic/demo implementation. Passwords are currently stored as plain values in the basic CRUD API; for a production system, implement Spring Security, password hashing, JWT/session authentication, DTOs, role-based authorization, and stronger validation.
