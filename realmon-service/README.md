# RealMon Service (Backend)

This is the backend service for **RealMon GO**, a location-based mobile app designed to build long-term nature connectedness through playful, sensory-rich, and emotionally engaging experiences.

---

## 🌿 Features

- RESTful API to manage **RealMon** entities
- Upload and retrieve sightings with **location**, **timestamp**, and **AI-assisted species info**
- Connected to **MySQL** via Spring Data JPA
- Ready for **Docker deployment** and **AWS EC2 hosting**

---

## 📦 Tech Stack

- Java 17 + Spring Boot
- MySQL 8
- Docker + Docker Compose
- Maven
- RESTful API (with JSON payloads)
- AWS EC2 (for future deployment)

---

## 📁 Project Structure

```
realmon-service/
├── controller/     # REST endpoints
├── model/          # Entity classes (RealMon, etc.)
├── repository/     # JPA repositories
├── service/        # (Optional) business logic
├── dto/            # (Optional) request/response models
├── util/           # (Optional) utilities or constants
├── application.yml # DB + JPA config
├── Dockerfile      # Containerize Spring Boot app
```

---

## 🚀 Run with Docker Compose

Make sure [Docker](https://www.docker.com/) is installed, then run:

```bash
docker-compose up --build
```

This will start:
- MySQL container
- RealMon Spring Boot backend

Visit: `http://localhost:8080/api/realmons`

---

## 🧪 Example API Endpoints

- `GET /api/realmons` – Get all RealMons
- `POST /api/realmons` – Create new RealMon
- `GET /api/realmons/{id}` – Get detail by ID

---

## 🌍 Next Step (Planned)

- [ ] AI Integration to identify species
- [ ] Upload image with location and time
- [ ] Integration with Wikipedia API
- [ ] Secure endpoints with Auth (Firebase or Spring Security)

---

## 🤖 Author

Developed by **Lina Wu**  
Thesis Project @ NUIG  
2024–2025

---

## 📄 License

MIT