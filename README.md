# 📚 Internal Knowledge Management API

A Spring Boot + PostgreSQL backend service for managing internal knowledge through **Articles**, **Departments**, and **Tags**.  
Includes search, versioning, and role-based access control.

---

## ✨ Features

- **Articles**
    - CRUD operations
    - Version history maintained
    - Linked to departments and tags
- **Departments**
    - Organize articles by department
- **Tags**
    - Free-form categorization
    - Auto-create missing tags
- **Search**
    - Keyword, title, department, tags, and ID filters
    - Pagination & sorting
- **Security**
    - Roles: `ADMIN`, `CONTRIBUTOR`, `VIEWER`
    - Currently open (`permitAll`) in dev mode
- **Database**
    - PostgreSQL with Flyway migrations
- **API Docs**
    - Swagger UI → `/swagger-ui/index.html`

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL 16
- Flyway
- Maven
- Lombok
- Springdoc OpenAPI

---

## 📂 Project Structure

```
src/main/java/org/godigit/ikm
├── KmApiApplication.java
├── config/           # OpenAPI + CORS configs
├── security/         # Security configuration
├── controller/       # REST controllers
├── service/          # Business logic layer
├── repository/       # JPA repositories
├── entities/         # JPA entities (Article, Department, Tag, User, Role)
├── dto/              # Request/Response DTOs
├── mapper/           # Entity ↔ DTO mappers
└── exception/        # Exception handlers
resources/
├── application.yml   # Config
└── db/migration/     # Flyway SQL migrations
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL 14+

### Configure DB
Update `src/main/resources/application.yml`:

```yamlpring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kmapi
    username: km
    password: km
  jpa:
    hibernate:
      ddl-auto: update
  flyway:
    enabled: true
    baseline-on-migrate: true
server:
  port: 8080
```

### Run the app
```bash
mvn spring-boot:run
```

Access Swagger UI:  
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📑 API Endpoints

### Articles
| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST   | `/api/articles` | Create article | Admin, Contributor |
| GET    | `/api/articles` | List all articles | All |
| GET    | `/api/articles/{id}` | Get by ID | All |
| PUT    | `/api/articles/{id}` | Update article | Admin, Contributor |
| DELETE | `/api/articles/{id}` | Delete article | Admin |

### Search
- `POST /api/search` → advanced filters + pagination
- `GET /api/search/keyword?keyword=...` → keyword search

Example request:
```json
{
  "keyword": "engineer",
  "departmentCode": "ENG",
  "tags": ["onboarding"],
  "page": 0,
  "size": 10,
  "sort": "createdAt,desc"
}
```

---

## 🧪 Testing

Run:
```bash
mvn test
```

Tests are under: `src/test/java/org/godigit/ikm/service/`

---

## 🐳 Docker Support

`docker-compose.yml` contains services:
- **db** → PostgreSQL
- **pgadmin** → DB admin UI
- **api** → Spring Boot service

Run with:
```bash
mvn package -DskipTests
docker compose up --build
```

---

## 🤝 Contributing

1. Fork the repo & branch (`git checkout -b feature/x`)
2. Add your feature/fix with tests
3. Commit (`feat: add x`)
4. Push and open a PR

---

