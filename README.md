# PrepTrack - Interview Preparation Platform

PrepTrack is a full-stack web application that helps students and software engineers prepare for technical interviews, campus placements, and company assessments.

## Technology Stack

### Frontend
- React 18
- Vite
- Tailwind CSS
- React Router
- Axios
- Lucide React

### Backend
- Java 21
- Spring Boot 3.5
- Spring Security with JWT authentication
- Spring Data JPA and Hibernate
- MySQL 8
- Maven
- Lombok

## Features

### Authentication
- Register and sign in with validated account details.
- Use stateless JWT authentication and BCrypt password hashing.
- Try the demo account from the login page.

### Dashboard
- View question-solving progress, mock-test results, and preparation statistics.
- Review topic progress and recent test attempts.

### Company Preparation
- Explore preparation information for TCS, Amazon, Infosys, Accenture, Google, Microsoft, Wipro, and Cognizant.
- Review hiring stages, exam patterns, eligibility details, and company-related questions.

### Question Bank
- Browse aptitude, reasoning, verbal ability, Java, DBMS, SQL, operating systems, networking, and DSA questions.
- Practice a 100-question Aptitude bank and a 100-question CSE Core bank covering Java/OOP, DBMS/SQL, operating systems, and networks.
- Search and filter questions by category, difficulty, and company.
- Use per-question hint and solution tabs, bookmark questions, and mark questions as solved.

### DSA Practice
- Browse a bank of 150 DSA practice questions across easy, medium, and hard difficulty levels.
- Review each problem statement and its suggested approach.
- Open each problem directly on LeetCode when a direct link is available. Otherwise, search for it in LeetCode's problem list.
- Track solved problems and bookmarks.

### Mock Tests
- Take timed company, core computer science, and DSA assessments.
- Take 10 distinct placement mocks, each with 10 Aptitude and 10 CSE Core questions and a 30-minute time limit. Questions do not repeat across these 10 tests.
- Open hints during a placement mock and review correct answers and solutions after submitting.
- Review scores, answers, and explanations after submission.
- The DSA mock test contains 15 questions selected from the DSA practice bank.

### Candidate Profile
- Manage profile details, review test history, and access bookmarked questions.

## Database

PrepTrack uses a MySQL database named `preptrack_db`. Create it before starting the backend:

```sql
CREATE DATABASE preptrack_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

The local `backend/src/main/resources/application.properties` file is ignored by Git because it can contain credentials. Create it from the safe example:

```powershell
Copy-Item backend\src\main\resources\application.example.properties backend\src\main\resources\application.properties
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
$bytes = New-Object byte[] 64
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
$rng.Dispose()
```

Keep your database password and JWT secret private. Each developer should use their own local values. Do not commit `application.properties` or `.env` files.

## Run Everything with Docker Compose

Docker Compose starts MySQL, the Spring Boot backend, and the Vite frontend together. It creates a separate local database for each developer's computer; it does not connect to another developer's MySQL database.

Requirements: Docker Desktop (Windows/macOS) or Docker Engine with the Docker Compose plugin.

From the project root in PowerShell:

```powershell
Copy-Item .env.example .env
```

Edit `.env` and replace both placeholder values. `MYSQL_ROOT_PASSWORD` is the local MySQL password. `JWT_SECRET` must be a Base64-encoded random key containing at least 32 bytes. For example, generate one in PowerShell:

```powershell
$bytes = New-Object byte[] 64
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
[Convert]::ToBase64String($bytes)
$rng.Dispose()
```

Copy the printed key into `.env` as the value of `JWT_SECRET`, then start the application:

```powershell
docker compose up --build
```

Open `http://localhost:5173`. The backend is available at `http://localhost:8080`. MySQL is only reachable by the Compose services, not exposed on a host port. The named database volume preserves local data when containers stop or are rebuilt. Stop the services with `Ctrl+C`, or run `docker compose down` in another terminal. **Do not use `docker compose down -v` unless you intend to delete the local database and all its data.**

The database seeds the question bank and mock tests when the backend starts. A first run builds the images and downloads dependencies, so it may take a few minutes.

## Requirements

- Docker Desktop or Docker Engine with the Docker Compose plugin (for the all-in-one setup)
- Java 21
- Apache Maven 3.9 or later
- Node.js 18 or later and npm
- MySQL Server 8

## Run the Application

### Start the Backend

In a PowerShell terminal:

```powershell
cd backend
mvn spring-boot:run
```

The backend runs at `http://localhost:8080`. On startup, it creates or updates the database tables and adds any missing seed data.

### Start the Frontend

Open a second terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open the local address printed by Vite, usually `http://localhost:5173`.

## Demo Account

- Username or email: `demo` or `demo@preptrack.com`
- Password: `password123`

You can also use the demo sign-in option on the login page.
