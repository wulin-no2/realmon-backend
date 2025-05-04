# RealMon GO Backend

This is the backend service for RealMon GO, a gamified nature observation app powered by Spring Boot and MySQL.

---

## 📦 Project Structure

- `realmon-service`: Main backend service.
- `realmon-common`: Shared models/utilities.
- `docker-compose.yml`: Local Docker environment for MySQL and backend.

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/realmon-backend.git
cd realmon-backend
```

### 2. Configure Environment

Copy example configuration files and customize your local setup:

```bash
cp realmon-service/src/main/resources/application-local.example.yml realmon-service/src/main/resources/application-local.yml
cp realmon-service/src/main/resources/application-docker.example.yml realmon-service/src/main/resources/application-docker.yml
```

Then edit these files to fit your environment (e.g., database password).

> ❗ **Do not commit `application-local.yml` or `application-docker.yml`.** They are listed in `.gitignore`.

---

## 🐳 Running with Docker

```bash
docker-compose up --build
```

This will start:

- MySQL at `localhost:3306`
- Backend server at `localhost:8080`

---

## 🧪 Development

To run locally without Docker, ensure you have a MySQL database running and configured in `application-local.yml`, then:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

---

## 📄 License

MIT License
