# PrepTrack – Interview Preparation Platform

A full-stack, enterprise-grade interview preparation platform designed to help students and software engineers prepare for technical placements, campus recruitment, and product company interviews (TCS, Infosys, Wipro, Accenture, Amazon, Google, Microsoft, and Cognizant).

---

## 🛠️ Technology Stack

### **Frontend**
- **React.js 18** with Modern Hooks & Functional Components
- **Vite** for ultra-fast bundling and HMR
- **Tailwind CSS 3** with modern dark-slate SaaS design system
- **React Router 6** for client-side routing & protected routes
- **Axios** with request/response interceptors and JWT authorization headers
- **Lucide React** for high-clarity iconography

### **Backend**
- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Security 6** with Stateless JWT Authentication & BCrypt password hashing
- **Spring Data JPA & Hibernate**
- **MySQL 8** relational database with indexes and schema relationships
- **Maven**
- **Lombok**

---

## 🚀 Key Modules & Features

### 1. Authentication & Security
- User registration and login with input validation.
- Stateless JWT-based authentication with expiration handling.
- Secure password hashing using BCrypt (`BCryptPasswordEncoder`).
- Client-side protected route wrappers and Axios authorization interceptors.
- Demo one-click login button for fast testing.

### 2. Candidate Dashboard
- **Total Questions Solved**: live progress bar tracking solved vs total questions.
- **Tests Attempted**: counter of completed mock assessments.
- **Average Test Score**: real-time accuracy and score tracking across all attempts.
- **Overall Preparation Readiness %**: calculated based on test performance and question mastery.
- **Topic-Wise Mastery Cards**: visual completion bars for Quant, Reasoning, Verbal, Java, DBMS, OS, Networks, and DSA.
- **Recent Test History Table**: past mock attempts with score, accuracy %, time taken, status, and direct links to full question analysis.

### 3. Company Preparation Hub
- Curated prep for **TCS, Amazon, Infosys, Accenture, Google, Microsoft, Wipro, and Cognizant**.
- **Interactive Recruitment Roadmap**: visual stage-by-stage hiring flowchart (Online Assessment -> Technical Interview -> Managerial/HR).
- **Exam Pattern & Syllabus Breakdown**: specific syllabus and time limits for each company.
- **Package & Roles**: CTC ranges (e.g. TCS Ninja/Digital/Prime, Amazon SDE-1) and eligibility criteria.
- **Company-Specific Question Bank**: filterable by Aptitude, Technical CS, and DSA with instant practice mode.
- **Company Mock Tests**: direct access to company-tailored tests.

### 4. Comprehensive Question Bank
- Core subjects covered:
  - Quantitative Aptitude & Logical Reasoning
  - Verbal Ability & English Comprehension
  - Java & Object-Oriented Programming (OOP)
  - Database Management Systems (DBMS) & Normalization
  - SQL Queries (Joins, Aggregations, Subqueries)
  - Operating Systems (Deadlocks, Scheduling, Virtual Memory)
  - Computer Networks (OSI, TCP 3-Way Handshake, HTTP/S)
  - Data Structures and Algorithms (DSA)
- **Interactive Practice Mode**: click options to immediately check if your choice is correct (green) or wrong (red), with full conceptual explanation revealed.
- **Bookmarks**: save questions to your personal revision list with instant database persistence.
- **Mark as Solved**: toggle question completion status.
- Search and multi-criteria filters (Category, Difficulty, Company tags).

### 5. DSA Practice Sheets
- Includes 150 coding-practice problems across common interview topics, seeded into the database on startup.
- The DSA mock test samples 15 questions from the DSA practice bank.
- Topic coverage: Arrays, Strings, Binary Search, Linked Lists, Trees, Graphs, Greedy, and Dynamic Programming.
- Difficulty levels: Easy, Medium, Hard.
- Company tags for every problem.
- **Optimal Approach Modal**: problem statement, constraints, example input/output, Java solution code snippet, and time/space complexity analysis.
- Direct external links to LeetCode practice problems.

### 6. Mock Test & Assessment System
- Timed MCQ tests with live countdown timer and auto-submission when time expires.
- **Anti-Cheat Answer Protection**: correct answers and explanations are **never** sent to the client during an exam.
- **Exam Workspace**:
  - Split layout with active question, options, and actions (Previous, Next, Clear Response, Mark for Review).
  - **Interactive Question Palette**: color-coded exam dots matching national testing standards:
    - 🟢 Green: Answered
    - 🟡 Yellow: Marked for Review
    - 🔵 Blue: Answered & Marked for Review
    - ⚪ Gray: Not visited
  - Live summary legend.
- **Automatic Backend Scoring**:
  - Evaluates answers against real database question records on submission.
  - Generates detailed score report: Total Score, Percentage, Correct count, Incorrect count, Unattempted count, Time taken, and Pass/Fail status.
  - **Comprehensive Question-by-Question Review**: review each question with your chosen answer vs the correct answer, accompanied by in-depth step-by-step conceptual explanations.

### 7. Candidate Profile & Progress
- Candidate Profile: target company, target role, graduation year, and edit capability.
- Test attempt history archive with review result links.
- Personal bookmarked questions list.

---

## 🗄️ Database Architecture

MySQL database: `preptrack_db`

### Tables & Relationships:
1. `users`: Stores candidate credentials, target company, target role, and graduation year.
2. `companies`: Company profile, logo, hiring roadmap, eligibility, and syllabus.
3. `topics`: Core preparation domains (Aptitude, Java, DBMS, OS, Networks, DSA).
4. `questions`: Questions with 4 options, correct answer, explanation, difficulty, and company tags.
5. `tests`: Mock assessments with duration, total marks, and passing criteria.
6. `test_questions`: Mapping of tests to questions with order and marks.
7. `test_attempts`: User test submissions with score, percentage, counts, and time taken.
8. `user_answers`: Individual question answers per attempt with `isCorrect` and `selectedOption`.
9. `bookmarks`: User's bookmarked questions.
10. `user_progress`: User's solved question status and timestamp.

---

## 🏃 Running the Application Locally

### Prerequisites
- **Java 21**
- **Apache Maven 3.9+**
- **Node.js 18+** and **npm**
- **MySQL Server 8.0**

### 1. Database Configuration
Ensure MySQL 8 is running on port 3306.
Create the database:
```sql
CREATE DATABASE preptrack_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Configure your credentials in `backend/src/main/resources/application.properties` if different from defaults:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/preptrack_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Anish@5671
```

### 2. Run the Backend (Spring Boot)
Open a terminal in the `backend/` directory:
```bash
mvn clean compile
mvn spring-boot:run
```
The backend will automatically:
- Connect to MySQL and create/update all 10 schema tables.
- Seed the demo user, 8 companies, 9 topics, 150 DSA practice questions plus other questions, and 5 mock tests.
- Listen on `http://localhost:8080`.

### 3. Run the Frontend (React + Vite)
Open a new terminal in the `frontend/` directory:
```bash
npm install
npm run dev
```
The frontend will start at `http://127.0.0.1:5173/`.

---

## 🔑 Demo Account Credentials
- **Username / Email**: `demo` or `demo@preptrack.com`
- **Password**: `password123`
*(Or click the "One-Click Login as Demo User" button on the login page)*
