# Placify - Interview Preparation Platform

Placify is a full-stack web application that helps students and software engineers prepare for technical interviews, campus placements, and company assessments.

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
- PostgreSQL 16
- Maven
- Lombok

## Features

### Authentication
- Register and sign in with validated account details.
- Use stateless JWT authentication and BCrypt password hashing.
- Request a password-reset link by email; links expire after 15 minutes and can only be used once.
- Try the demo account from the login page.

### Dashboard
- View question-solving progress, mock-test results, and preparation statistics.
- Review topic progress and recent test attempts.

### Company Preparation
- Explore preparation information for  TCS, Amazon , Infosys, Accenture, Google, Microsoft, Wipro, and Cognizant .
- Review hiring stages, exam patterns, eligibility  details, and company-related questions.

### Question Bank
- Browse aptitude, reasoning, verbal ability, Java, DBMS, SQL, operating systems, networking, and DSA questions.
- Practice 15 question banks: 10 Aptitude sets and 5 CSE Core sets, with 30 questions per set (300 Aptitude and 150 CSE Core questions total). These are original company-style practice questions, not verified company PYQs. They link to IndiaBIX or GeeksforGeeks topic references and show companies as practice targets, not as confirmed past appearances.
- Search and filter questions by category, difficulty, and company.
- Use per-question hint and solution tabs, bookmark questions, and mark questions as solved.

### DSA Practice
- Browse 220 practice problems in 11 topic modules, with 20 problems in each module.
- Review original problem summaries and suggested approaches without difficulty labels.
- Problems are curated from important LeetCode, GeeksforGeeks, and Striver practice patterns; question summaries are original and no sheet  links are displayed.
- Per-problem links search LeetCode for the relevant problem.
- Track solved problems and  bookmarks.

### Mock Tests
- Take timed company, core computer science, and DSA assessments.
- Take 10 distinct placement mocks, each with 10 Aptitude and 10 CSE Core questions and a 30-minute time limit. Questions do not repeat across these 10 tests.
- Open hints during a placement mock and review correct answers and solutions after submitting.
- Review scores, answers, and explanations after submission.
- The DSA mock test contains 15 questions selected from the DSA practice bank.

### 1v1 Coding Battle
- Create an authenticated private room, select one of 50 original coding problems, and choose a 5–120 minute match.
- Share the room code with a friend. The shared server timer starts when the opponent joins; room status and winner are synchronized through the API.
- Write and run C++, Java, or Python 3 solutions in the in-app editor. Run checks the two visible examples; Submit checks eight private edge cases.
- Hidden inputs and expected outputs stay on the backend. Submitted code is sent to Judge0 CE for sandboxed execution and is never executed on the Spring Boot host.
- The default provider is the public `https://ce.judge0.com` instance. Its free tier may be busy or rate-limited. Configure `JUDGE0_BASE_URL` and, if required by your provider, `JUDGE0_AUTH_TOKEN` in `.env`.

### Candidate Profile
- Manage profile details, review test history, and access bookmarked questions.

### Resume Analyzer
- Upload a PDF or DOCX resume with drag-and-drop validation and a 10 MB limit.
- Extract resume text securely in memory and analyze it with a configurable Gemini model.
- Review transparent score breakdowns, grouped skills, ATS guidance, strengths, weaknesses, and evidence-based improvements.
- Optionally paste a job description to compare matching and missing skills without recommending skills absent from the resume.
- Download a PDF report. Only structured analysis results are saved to the authenticated user's history; uploaded resume contents are not persisted.

## Database

Placify uses PostgreSQL and creates/updates its schema on backend startup. The tracked `backend/src/main/resources/application.properties` contains no credentials; supply database and JWT settings through environment variables. For local Docker Compose, configure the ignored root `.env` file. For a manual backend run, set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, and `APP_JWT_SECRET` in the terminal.

To restore the tracked local template if it is missing:

```powershell
Copy-Item backend\src\main\resources\application.example.properties backend\src\main\resources\application.properties
```

Keep your database password and JWT secret private. Each developer should use their own local values. Never commit `.env` files or put credentials in frontend environment variables.

The default PostgreSQL database, user, and Compose volume identifiers are intentionally unchanged so an existing local database and its data continue to be reused. Do not rename these identifiers unless you also migrate the database and volume.

Password reset email requires SMTP configuration. Set `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `SMTP_FROM`, and `APP_FRONTEND_URL` in the backend environment (`APP_FRONTEND_URL` must be the public frontend origin when deployed). `SMTP_AUTH` and `SMTP_STARTTLS` default to `true`. If SMTP is not configured, password-reset requests return an explicit service-unavailable error. To avoid disclosing whether an email is registered, requests use a generic response; mail delivery failures are logged on the backend.

## Run Everything with Docker Compose

Docker Compose starts PostgreSQL, the Spring Boot backend, and the Vite frontend together. It creates a separate local database for each developer's computer; it does not connect to another developer's database.

Requirements: Docker Desktop (Windows/macOS) or Docker Engine with the Docker Compose plugin.

From the project root in PowerShell:

```powershell
if (!(Test-Path .env)) { Copy-Item .env.example .env }
```

Edit `.env` and set `POSTGRES_PASSWORD` to a strong local database password. Keep `POSTGRES_USER`, `POSTGRES_DB`, and the local CORS origins as provided unless you want to customize them. `JWT_SECRET` must be a Base64-encoded random key containing at least 32 bytes. For example, generate one in PowerShell:

```powershell
$bytes = New-Object byte[] 64
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
[Convert]::ToBase64String($bytes)
$rng.Dispose()
```

Copy the printed key into `.env` as the value of `JWT_SECRET`. If you previously ran the MySQL Compose setup, stop its containers without deleting its saved database volume, then start the PostgreSQL stack:

```powershell
docker compose down --remove-orphans
docker compose up --build
```

Open `http://localhost:5173`. The frontend calls the local backend at `http://localhost:8080/api` by default. If your existing `.env` still sets `VITE_API_BASE_URL` to a hosted backend, change it to `http://localhost:8080/api` for local Compose runs. The backend is available at `http://localhost:8080`. PostgreSQL is only reachable by the Compose services, not exposed on a host port. The named PostgreSQL volume preserves data when containers stop or are rebuilt. The old MySQL volume is not automatically converted or deleted; the new PostgreSQL database starts with the application's seeded data. Stop services with `Ctrl+C`, or run `docker compose down`. **Do not use `docker compose down -v` unless you intend to delete the PostgreSQL data volume.**

The database seeds the question banks and mock tests when the backend starts. Question seeding is repeatable and fills missing catalog questions without deleting existing user progress. A first run builds the images and downloads dependencies, so it may take a few minutes.

## Requirements

- Docker Desktop or Docker Engine with the Docker Compose plugin (for the all-in-one setup)
- Java 21
- Apache Maven 3.9 or later
- Node.js 18 or later and npm
- PostgreSQL 16 (for a manual, non-Docker setup)

## Run the Application

### Start the Backend

In a PowerShell terminal:

```powershell
cd backend
mvn spring-boot:run
```

The backend runs at `http://localhost:8080`. On startup, it creates or updates the database tables and adds any missing seed data.

### Start the Frontend

Install PostgreSQL locally, create the database configured by `POSTGRES_DB` in `.env.example`, and set the `DB_USERNAME` and `DB_PASSWORD` environment variables as described above. Then open a second terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open the local address printed by Vite, usually `http://localhost:5173`.

## Deploy on Render with Supabase

The repository includes a Render Blueprint at `render.yaml` that deploys the Spring Boot backend and Vite frontend as separate Render services. PostgreSQL is hosted on Supabase; no database or application secrets are stored in the repository. The GitHub Actions workflow at `.github/workflows/ci.yml` runs Maven tests and the Vite production build for pull requests and pushes to `main` or `master`.

### Supabase database

1. Create a Supabase PostgreSQL project.
2. In the Supabase dashboard, open the database connection details and select the **Session pooler** connection.
3. Use its host, port, database, and username to configure Render's environment variables. Build the JDBC URL as `jdbc:postgresql://<pooler-host>:<port>/<database>?sslmode=require`. Keep the pooler username and database password private.

### Render services

1. Connect the GitHub repository to Render and create a Blueprint from the root `render.yaml`. It creates a backend web service and a static frontend site; both deploy from `main`.
2. Set these backend environment variables in the Render dashboard:
   - `SPRING_DATASOURCE_URL`: Supabase session-pooler JDBC URL with `sslmode=require`.
   - `SPRING_DATASOURCE_USERNAME`: pooler username from Supabase.
   - `SPRING_DATASOURCE_PASSWORD`: database password from Supabase.
   - `APP_JWT_SECRET`: a newly generated Base64-encoded random key containing at least 32 bytes.
   - `APP_CORS_ALLOWED_ORIGINS`: exact Render frontend origin, without a trailing slash. Set this after the frontend service is created.
   - `APP_FRONTEND_URL`: same frontend origin, used to build password-reset links.
   - `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, and `SMTP_FROM`: mail provider settings for password resets.
   - `SMTP_AUTH` and `SMTP_STARTTLS`: optional SMTP settings; both default to `true`.
   - `JUDGE0_AUTH_TOKEN`: optional; only set this if the chosen Judge0 provider requires it.
   - `GEMINI_API_KEY`: Google Gemini API key for Resume Analyzer. Keep it backend-only.
   - `GEMINI_MODEL`: configurable Gemini model name; defaults to `gemini-2.5-flash`.
   - `GEMINI_BASE_URL`: defaults to `https://generativelanguage.googleapis.com`.
   - `RESUME_MAX_FILE_SIZE_BYTES`: upload limit, default `10485760` (10 MB).
3. Set the frontend service's `VITE_API_BASE_URL` environment variable to the backend's public URL ending in `/api` (for example, `https://preptrack-backend.onrender.com/api`). The Blueprint marks this value as `sync: false` because the actual backend URL is assigned by Render.
4. After both services have deployed, set the backend's `APP_CORS_ALLOWED_ORIGINS` and `APP_FRONTEND_URL` to the frontend's actual Render URL, without a trailing slash, then redeploy the backend.
5. The backend health check uses `GET /api/questions`. The static site rewrites client-side routes to `index.html`. Do not add `GEMINI_API_KEY` or other secrets to the frontend service; the browser calls the backend, which calls Gemini.
6. To enable Gemini feedback, create a key in Google AI Studio, confirm the Generative Language API and selected model are available to its project, and set `GEMINI_API_KEY` on the Render backend. `GEMINI_MODEL` defaults to `gemini-2.5-flash`.

The free Render web service is suitable for testing and hobby projects, not production: it spins down after 15 minutes without traffic and its first request after that can take about a minute. Check Render's current billing dashboard and do not select a paid plan if you do not want charges.

### Continuous integration and automatic deployments

Both Render services are configured to deploy automatically from `main`.

For CI-gated production deployments, configure GitHub branch protection for `main`: require pull requests and require both `Backend tests` and `Frontend build` status checks before merging. Deployments then happen after a passing PR is merged. Test the live frontend, login, database-backed question list, and a coding battle after initial setup.

Never put Supabase credentials, JWT signing keys, Gemini API keys, or Judge0 tokens in YAML, frontend `VITE_` variables, or committed files. If a credential was previously committed, rotate it in its provider dashboard.

### Resume Analyzer API

All endpoints require the existing `Authorization: Bearer <JWT>` header:

- `POST /api/resume/analyze` accepts `multipart/form-data` with a `file` (PDF or DOCX, up to 10 MB) and optional `jobDescription` (up to 12,000 characters). It returns a transparent local checklist score, skills detected from a curated catalog, up to three skill-evidence-based role matches, and an optional match against the supplied job description. It does not persist the resume or analysis.

Gemini supplies optional written feedback only. If its key is missing, denied, rate-limited, or unavailable, local scoring and job matching still return; the response's `aiFeedback.status` and `aiFeedback.message` explain why AI feedback was unavailable.

Set `GEMINI_API_KEY` in the ignored root `.env` for Docker Compose or in the backend process environment for a manual run. For Render deployments, set it only on the backend service. The frontend never receives or stores this key. Resume text is sent to Google Gemini only when AI feedback is enabled and the backend key is configured; tell users before upload.

### Screenshots

Add deployment screenshots here as the Resume Analyzer UI is promoted between environments. The feature is available at `/resume-analyzer` after sign-in.

## Demo Account

- Username: `demo`
- Password: `password123`

You can also use the demo sign-in option on the login page.
