# Employee Management System

Standalone Spring Boot REST API for managing employees. It uses an in-memory H2 database, so no database server is required and data is reset when the application stops.

## Run

Requires Java 8 or later.

```powershell
.\mvnw.cmd spring-boot:run
```

## API

Base URL: `http://localhost:8080/employees`

| Method | Path | Action |
| --- | --- | --- |
| GET | `/employees` | List employees |
| GET | `/employees/{id}` | Get one employee |
| POST | `/employees` | Create an employee |
| PUT | `/employees/{id}` | Replace an employee |
| PATCH | `/employees/{id}` | Partially update an employee |
| DELETE | `/employees/{id}` | Delete an employee |
| GET | `/employees/department/{dept}` | List employees by department |
| GET | `/employees/salary/{amount}` | List employees with salary above amount |
| GET | `/employees/search?name={name}` | Search employee names (case-insensitive substring) |

Example request body:

```json
{
  "name": "Alex Morgan",
  "email": "alex.morgan@example.com",
  "department": "Engineering",
  "salary": 75000,
  "joiningDate": "2024-06-15"
}
```

All responses use an envelope containing `timestamp`, `message`, `data`, and `status`. Employee names and departments must not be blank, email addresses must be valid and unique, salary must be greater than zero, and joining date cannot be in the future. Invalid input returns `400`, duplicate email returns `409`, and an unknown employee id returns `404`.
