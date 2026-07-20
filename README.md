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

### 2. Use Java 17

The build enforces Java 17. Check the active JVM before running Maven:

```bash
java -version
./mvnw -version
```

### 3. Configure Environment

Copy example configuration files and customize your local setup:

```bash
cp realmon-service/src/main/resources/application-local.example.yml realmon-service/src/main/resources/application-local.yml
cp realmon-service/src/main/resources/application-docker.example.yml realmon-service/src/main/resources/application-docker.yml
```

Then edit the local file to fit your environment. At minimum, configure the MySQL datasource, JWT secret, and OpenAI settings used by AI species details.

> ❗ **Do not commit `application-local.yml` or `application-docker.yml`.** They are listed in `.gitignore`.

---

## 🐳 Running with Docker

Create a root `.env` file or export secure values in your shell:

```bash
MYSQL_ROOT_PASSWORD=replace-with-a-strong-password
JWT_SECRET=replace-with-at-least-32-random-bytes
OPENAI_APIKEY=replace-with-your-api-key
```

Docker Compose refuses to start when `MYSQL_ROOT_PASSWORD` or `JWT_SECRET` is missing.

```bash
docker compose up --build
```

This will start:

- MySQL at `localhost:3306`
- Backend server at `localhost:8080`

---

## 🧪 Development

To run locally without Docker, ensure MySQL is running and `application-local.yml` is configured, then build and run the executable service jar:

```bash
./mvnw package
java -jar realmon-service/target/realmon-service-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=local
```

The multi-module root is an aggregator, so running `spring-boot:run` directly at the root is not supported.

To load the optional sample data, add `demo` to the active profiles. The normal `local` and `docker` profiles never clear or reseed the `realmon` table.

## 🧪 Tests

Tests use an isolated in-memory H2 database and do not require local MySQL:

```bash
./mvnw test
```

## 🌐 Frontend development

The backend accepts browser requests from local `localhost` and `127.0.0.1` development ports. Additional deployed frontend origins can be supplied as a comma-separated environment property:

```bash
APP_CORS_ALLOWED_ORIGIN_PATTERNS=https://app.example.com
```

## 🔐 Administrator access

New and existing accounts have user-level access by default. Promote the first administrator directly in MySQL after that account has registered:

```sql
UPDATE `user` SET role = 'ADMIN' WHERE username = 'your-admin-username';
```

Administrative endpoints require the `ADMIN` role. Registration requests cannot assign roles.

## 🛡 External-service protection

The backend limits each client to 6 scan requests per minute and each authenticated user to 3 AI species-detail generations per minute. Override these limits when needed:

```bash
SCAN_REQUESTS_PER_MINUTE=6
GENERATION_REQUESTS_PER_MINUTE=3
```

Outbound REST calls use a 5-second connection timeout and a 15-second read timeout. Rate-limit counters are stored in memory; use a shared Redis-backed limiter before running multiple backend instances.

---

## 📄 License

MIT License
