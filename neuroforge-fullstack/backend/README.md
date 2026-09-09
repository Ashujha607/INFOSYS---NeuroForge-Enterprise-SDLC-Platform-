# NeuroForge Enterprise - Backend

Spring Boot + MySQL REST API for an SDLC management platform.

## Modules
- Users and roles
- Projects
- Sprints
- Tasks
- Bug reports

## Requirements
- Java 17+
- Maven 3.9+
- MySQL 8+

## Setup
1. Create database: `CREATE DATABASE neuroforge;`
2. Edit `src/main/resources/application.properties` and set your MySQL password.
3. Run: `mvn spring-boot:run`
4. Server: `http://localhost:8080`

## API endpoints
- GET/POST `/api/users`
- GET/POST `/api/projects`
- GET/POST `/api/sprints`
- GET/POST `/api/tasks`
- GET/POST `/api/bugs`

PUT and DELETE are available with `/{id}` for each resource.

## Example project JSON
`{"name":"NeuroForge","description":"Enterprise SDLC Platform","status":"ACTIVE"}`
