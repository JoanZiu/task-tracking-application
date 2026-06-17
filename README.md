# Task Tracking REST API

A RESTful API for managing users, projects, and tasks, built with Spring Boot. Supports full CRUD operations, validation, pagination, filtering, and centralized exception handling.

## Technologies

- **Java 21**
- **Spring Boot 4.1.0** (Spring Web MVC, Spring Data JPA)
- **H2** in-memory database
- **Flyway** for database migrations
- **springdoc-openapi** (Swagger UI) for API documentation
- **JUnit 5, Mockito** for testing
- **Maven** for build management
- **Lombok** for boilerplate reduction

## Prerequisites

- JDK 21 or higher
- Maven (or the included Maven wrapper `mvnw`)

## Setup & Running

1. Clone the repository:
   ```
   git clone <repository-url>
   cd task-tracking
   ```

2. Run the application:
   ```
   ./mvnw spring-boot:run
   ```
   (On Windows: `mvnw spring-boot:run`)

3. The application starts on `http://localhost:8080`.

The H2 in-memory database is created automatically on startup, and Flyway applies the schema migrations. Note: data is reset on each restart (in-memory database).

## API Documentation

Interactive API documentation is available via Swagger UI once the application is running:

```
http://localhost:8080/swagger-ui.html
```

## H2 Database Console

The H2 console is available at:

```
http://localhost:8080/h2-console
```

Connection settings:
- **JDBC URL:** `jdbc:h2:mem:tasktracker`
- **Username:** `sa`
- **Password:** (leave empty)

## Database Schema

The schema consists of three tables with the following relationships:

```
users
  - id (PK)
  - username
  - email
  - password
  - created_at

projects
  - id (PK)
  - name
  - description
  - created_at
  - owner_id (FK -> users.id)

tasks
  - id (PK)
  - title
  - description
  - status (TODO, IN_PROGRESS, COMPLETED)
  - priority (LOW, MEDIUM, HIGH)
  - due_date
  - created_at
  - project_id (FK -> projects.id)
  - assignee_id (FK -> users.id, nullable)
```

**Relationships:**
- A user can own many projects (one-to-many)
- A project belongs to one owner (many-to-one)
- A project can have many tasks (one-to-many)
- A task belongs to one project (many-to-one)
- A task can be assigned to one user, or none (many-to-one, nullable)

## API Endpoints

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a user |
| GET | `/api/users/{id}` | Get a user by ID |
| GET | `/api/users` | Get all users (paginated) |

### Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/projects` | Create a project |
| GET | `/api/projects/{id}` | Get a project by ID |
| GET | `/api/projects` | Get all projects (paginated) |
| PUT | `/api/projects/{id}` | Update a project |
| DELETE | `/api/projects/{id}` | Delete a project |

### Tasks
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/projects/{projectId}/tasks` | Create a task in a project |
| GET | `/api/tasks/{id}` | Get a task by ID |
| GET | `/api/projects/{projectId}/tasks` | Get tasks of a project (paginated, filter by `?status=`) |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |
| GET | `/api/tasks/due-today` | Get tasks due today |
| GET | `/api/users/{userId}/tasks` | Get tasks assigned to a user |

## Validation

Request bodies are validated using Jakarta Bean Validation annotations. Invalid input returns a `400 Bad Request` with field-specific error messages.

## Error Handling

Centralized exception handling via `@RestControllerAdvice` returns clean, structured error responses:
- `404 Not Found` — when a requested resource does not exist
- `400 Bad Request` — when validation fails
- `500 Internal Server Error` — for unexpected errors

Example error response:
```json
{
  "timestamp": "2026-06-14T16:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999"
}
```

## Testing

The project includes three types of automated tests:
- **Service layer (unit tests):** JUnit 5 + Mockito, mocking the repository layer
- **Repository layer:** `@DataJpaTest` with an embedded test database
- **Controller layer (integration tests):** `@WebMvcTest` with MockMvc

Run all tests with:
```
./mvnw test
```

## Assumptions

- Passwords are stored as provided (no hashing in the base version).
- The in-memory database resets on each restart; data does not persist.
- A task's assignee is optional; a task may exist without an assigned user.
- Pagination uses Spring Data's default page size (20) unless `?page` and `?size` query parameters are provided.
