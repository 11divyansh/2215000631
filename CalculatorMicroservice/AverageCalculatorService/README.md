# Number Service Application

This repository contains multiple microservices related to number processing tasks.

## Calculator Microservice

### Features:
- Calculates prime, even, Fibonacci, and random numbers.
- Maintains a rolling window of last 10 unique numbers.
- Calculates average of current window.

### Endpoints:
- `GET /numbers/p` – Prime numbers
- `GET /numbers/e` – Even numbers
- `GET /numbers/f` – Fibonacci numbers
- `GET /numbers/r` – Random numbers

---

## 🛠Technologies Used

- Java
- Spring Boot
- Maven
- REST APIs

---

## How to Run

```bash
cd calculator-service
./mvnw spring-boot:run
