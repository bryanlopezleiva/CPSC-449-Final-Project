# Movie Watch List
### CPSC 449 — Web Backend Engineering | Cal State Fullerton
### Final Project


## Team Members

| Name | CWID      |
|------|-----------| 
| Bryan Lopez Levia | 821009883 |
| Daniel DiPietrantonio | 831231386 |
| Ryan Oskuie | 885384289      |
| Roberto Manra | 868912841 |

---

## Prerequisites
- **Docker Desktop** v4.0 or higher — [Download](https://www.docker.com/products/docker-desktop/)
- **PostgreSQL** installed and running locally with a database named `final_project_db`
- **Postman** for testing the API endpoints

---

### Database Setup
Before running the application, make sure PostgreSQL is running and the database exists. In psql or pgAdmin run:
```sql
CREATE DATABASE final_project_db;
```

### Demo
```
Youtube Demo: https://youtu.be/4RbLARP2B9A?si=FUhhkkztgqzeZIcH
```

---

## Building the Docker Image
Run the following command from the project root directory (the same folder as `pom.xml` and `Dockerfile`):
```bash
docker build -t movie-watchlist:1.0 .
```

---

## Running the Application
Once the image is built, start the container with:
```bash
docker run -d --name movie-watchlist -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/final_project_db \
  -e SPRING_DATASOURCE_USERNAME=your_postgres_username \
  -e SPRING_DATASOURCE_PASSWORD=your_postgres_password \
  movie-watchlist:1.0
```
Replace `your_postgres_username` and `your_postgres_password` with your local PostgreSQL credentials.

---

### Verify the application started
```bash
docker logs movie-watchlist
```
The Spring Boot startup banner should be visible in the logs confirming the application is running on port 8080.

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT token |

### Movies (Protected — requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/movies` | Create a new movie |
| GET | `/api/movies` | Get all movies for the authenticated user |
| GET | `/api/movies/{id}` | Get a single movie by ID |
| PUT | `/api/movies/{id}` | Update a movie |
| DELETE | `/api/movies/{id}` | Delete a movie |

---

## Example Postman Requests

### 1. Register a new user
**POST** `http://localhost:8080/api/auth/register`

Request body:
```json
{
  "username": "roberto",
  "email": "roberto@example.com",
  "password": "securepassword123"
}
```
Expected response — **HTTP 201 Created**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Create a movie (Protected endpoint)
**POST** `http://localhost:8080/api/movies`

In Postman under the **Authorization** tab, select **Bearer Token** and paste the token received from register or login.

Request body:
```json
{
  "title": "Inception",
  "genre": "Sci-Fi",
  "releaseYear": 2010,
  "isWatched": false,
  "rating": 9.0
}
```
Expected response — **HTTP 201 Created**:
```json
{
  "id": 1,
  "title": "Inception",
  "genre": "Sci-Fi",
  "releaseYear": 2010,
  "isWatched": false,
  "rating": 9.0
}
```

---

## Stopping the Application
```bash
docker stop movie-watchlist
docker rm movie-watchlist
```
