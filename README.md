# Task Tracking REST API

A RESTful API for managing users, projects, and tasks, built with Spring Boot. Supports full CRUD operations, validation, pagination, filtering, centralized exception handling, authentication, email notifications, and dynamic search.

## Technologies

- **Java 21**
- **Spring Boot 4.1.0** (Spring Web MVC, Spring Data JPA, Spring Security, Spring Mail)
- **H2** in-memory database
- **Flyway** for database migrations
- **springdoc-openapi** (Swagger UI) for API documentation
- **JUnit 5, Mockito** for testing
- **Maven** for build management
- **Lombok** for boilerplate reduction
- **Axios** (frontend) for API communication

## Prerequisites

- JDK 21 or higher
- Maven (or the included Maven wrapper `mvnw`)

## Setup & Running

1. Clone the repository:
   ```
   git clone <repository-url>
   cd task-tracking
   ```

2. (For email notifications) Configure SMTP credentials in `src/main/resources/application.properties`:
   ```
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```
   Use a Gmail App Password (not your account password).

3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```
   (On Windows: `mvnw spring-boot:run`)

4. The application starts on `http://localhost:8080`.

On startup, Flyway applies the schema migrations and a data seeder creates an initial user for login.

## Frontend Dashboard

A dashboard built with HTML, CSS, and JavaScript (Axios) is served at:

```
http://localhost:8080/
```

Features: login screen, sidebar navigation, statistics cards, user/project/task management with create/edit/delete, task filtering by status, task search, and client-side form validation.

**Default login credentials (seeded):**
- Username: `joan`
- Password: `joan12345`

## Authentication

The API uses **HTTP Basic Authentication** (Spring Security). Passwords are hashed with BCrypt.

- `POST /api/users` (registration) is open without authentication.
- Swagger UI and the H2 console are accessible without authentication.
- All other endpoints require valid credentials.

To authenticate in Swagger UI, click "Authorize" and enter your username and password.

## API Documentation

Interactive API documentation via Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## H2 Database Console

```
http://localhost:8080/h2-console
```

Connection settings:
- **JDBC URL:** `jdbc:h2:mem:tasktracker`
- **Username:** `sa`
- **Password:** (leave empty)

## Database Schema

```
users
  - id (PK)
  - username (unique)
  - email
  - password (BCrypt hashed)
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
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/users` | Create a user (registration) | No |
| GET | `/api/users/{id}` | Get a user by ID | Yes |
| GET | `/api/users` | Get all users (paginated) | Yes |

### Projects
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/projects` | Create a project | Yes |
| GET | `/api/projects/{id}` | Get a project by ID | Yes |
| GET | `/api/projects` | Get all projects (paginated) | Yes |
| PUT | `/api/projects/{id}` | Update a project | Yes |
| DELETE | `/api/projects/{id}` | Delete a project | Yes |

### Tasks
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/projects/{projectId}/tasks` | Create a task in a project | Yes |
| GET | `/api/tasks/{id}` | Get a task by ID | Yes |
| GET | `/api/projects/{projectId}/tasks` | Get tasks of a project (paginated, filter by `?status=`) | Yes |
| PUT | `/api/tasks/{id}` | Update a task | Yes |
| DELETE | `/api/tasks/{id}` | Delete a task | Yes |
| GET | `/api/tasks/due-today` | Get tasks due today | Yes |
| GET | `/api/users/{userId}/tasks` | Get tasks assigned to a user | Yes |
| GET | `/api/tasks/search` | Search tasks (filter by `?title=`, `?status=`, `?priority=`) | Yes |

## Optional Features Implemented

1. **Authentication** — HTTP Basic Authentication with Spring Security and BCrypt password hashing.
2. **Email notifications** — when a task is assigned to a user, an email notification is sent via Spring Mail.
3. **Task search** — dynamic search using Spring Data JPA Specifications, filtering by any combination of title, status, and priority.

## Validation

Request bodies are validated using Jakarta Bean Validation annotations. Invalid input returns a `400 Bad Request` with field-specific error messages. The frontend also performs client-side validation.

## Error Handling

Centralized exception handling via `@RestControllerAdvice` returns clean, structured error responses:
- `404 Not Found` — when a requested resource does not exist
- `400 Bad Request` — when validation fails
- `401 Unauthorized` — when authentication is missing or invalid
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

Three types of automated tests:
- **Service layer (unit tests):** JUnit 5 + Mockito, mocking the repository layer
- **Repository layer:** `@DataJpaTest` with an embedded test database
- **Controller layer (integration tests):** `@WebMvcTest` with MockMvc

Run all tests with:
```
./mvnw test
```

## Assumptions

- The in-memory database resets on each restart; data does not persist between runs.
- A data seeder creates an initial user (`joan`) on startup for immediate login.
- A task's assignee is optional; a task may exist without an assigned user.
- Pagination uses Spring Data's default page size (20) unless `?page` and `?size` query parameters are provided.
- Passwords are hashed with BCrypt before being stored.
