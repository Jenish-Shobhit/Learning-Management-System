# Learning Management System

[![Build](https://github.com/Jenish-Shobhit/Learning-Management-System/actions/workflows/build.yml/badge.svg)](https://github.com/Jenish-Shobhit/Learning-Management-System/actions/workflows/build.yml)
![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 3.4.5](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?logo=springboot&logoColor=white)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A learning management system prototype with a Java and Spring Boot API, MySQL persistence, and a browser based frontend. It has student and instructor pages for courses, enrollments, progress, and quizzes.

> **Project status:** This is an educational prototype. Its API currently permits all requests. Read [Security and limitations](#security-and-limitations) before using it with real accounts or data.

## What is implemented

| Area | Current implementation |
| --- | --- |
| Users | Registration, password check at login, user endpoints, and password update |
| Courses | List, create, read, update, and delete courses |
| Enrollments | Enroll a user, list enrollments, and read or update course progress |
| Quizzes | Create, list, update, and delete quizzes and questions |
| Dashboards | Student progress and leaderboard views; instructor course, quiz, and student tracking views |

The frontend is plain HTML, CSS, and JavaScript. It loads Bootstrap 4, jQuery, Font Awesome 5, and Chart.js from CDNs. There is no frontend package manager or build step.

## Architecture

```text
LMS-frontend/  HTML + CSS + JavaScript (browser requests to localhost:2025)
       |
       v
LMS-backend/   controller -> service -> Repository -> MySQL
               model (JPA entities), dto, response, exceptions, security
```

The backend uses Java 21, Spring Boot 3.4.5, Spring Web, and Spring Data JPA. The configured database is MySQL. JPA repositories provide entity operations; `EnrollmentRepository` also contains native SQL for the leaderboard and progress tracking. Registration and password updates hash passwords with BCrypt, and login verifies them with BCrypt.

## Run locally

**Prerequisites:** JDK 21, MySQL, and Python 3 or another static file server. The Maven wrapper downloads Maven 3.9.16 on its first run.

1. Create a local database:

   ```sql
   CREATE DATABASE lmsdb;
   ```

2. Set your local MySQL credentials and start the API from the repository root:

   ```sh
   cd LMS-backend
   export DB_USERNAME="root"
   export DB_PASSWORD="your-local-password"
   ./mvnw spring-boot:run
   ```

   The defaults are `jdbc:mysql://localhost:3306/lmsdb` and port `2025`. Set `DB_URL` if your MySQL address or database name differs. The schema is managed by Hibernate with `ddl-auto=update`.

3. In another terminal, serve the frontend:

   ```sh
   cd LMS-frontend
   python3 -m http.server 5500 --bind 127.0.0.1
   ```

4. Open [http://127.0.0.1:5500/](http://127.0.0.1:5500/). The frontend calls `http://localhost:2025`; the backend CORS configuration allows the `127.0.0.1:5500` origin. CDN assets need an internet connection.

### Checks

```sh
cd LMS-backend
./mvnw verify
```

The Spring Boot context test needs a reachable MySQL database. [GitHub Actions](.github/workflows/build.yml) starts a disposable MySQL service for the backend test and checks JavaScript syntax in the frontend.

## API entry points

| Area | Routes |
| --- | --- |
| Login and registration | `POST /login`, `POST /register` |
| Users | `/api/users` |
| Courses | `/api/courses` (including `GET /api/courses/allCourses`) |
| Enrollments and progress | `/api/enrollments` |
| Quizzes | `/api/quizes` (spelling as implemented) |
| Questions | `/api/questions` |

## Security and limitations

- `SecurityConfig` has `anyRequest().permitAll()` and disables CSRF, form login, and HTTP Basic. The Spring Security dependency does **not** protect these API routes.
- `/login` checks a BCrypt hash and returns a user ID and role. It does not issue a JWT or create an authenticated session. The frontend stores the user ID in `localStorage`.
- `/updatePassword` accepts a user ID and a new password without proving control of that account. The `/api/users` create path also bypasses the BCrypt registration method. These paths need redesign before production use.
- The frontend calls hard-coded localhost API URLs; it is configured for local development rather than deployment to another host.

See [SECURITY.md](SECURITY.md) for vulnerability reporting. Contributions are welcome through [CONTRIBUTING.md](CONTRIBUTING.md). The project is available under the [MIT License](LICENSE).

## Verified in

- [Backend POM](LMS-backend/pom.xml) and [wrapper configuration](LMS-backend/.mvn/wrapper/maven-wrapper.properties) — Java, Spring Boot, dependencies, and Maven versions.
- [Application properties](LMS-backend/src/main/resources/application.properties) — MySQL configuration, environment overrides, Hibernate setting, and port.
- [Controllers](LMS-backend/src/main/java/com/project/LMS/controller), [services](LMS-backend/src/main/java/com/project/LMS/service), [repositories](LMS-backend/src/main/java/com/project/LMS/Repository), and [models](LMS-backend/src/main/java/com/project/LMS/model) — API routes, layers, entities, and features.
- [EnrollmentRepository](LMS-backend/src/main/java/com/project/LMS/Repository/EnrollmentRepository.java) — native SQL for leaderboard and tracking.
- [UserService](LMS-backend/src/main/java/com/project/LMS/service/UserService.java), [loginController](LMS-backend/src/main/java/com/project/LMS/controller/loginController.java), and [UserController](LMS-backend/src/main/java/com/project/LMS/controller/UserController.java) — password hashing, login response, and user-write paths.
- [SecurityConfig](LMS-backend/src/main/java/com/project/LMS/security/SecurityConfig.java) — `permitAll`, disabled security mechanisms, and local CORS origin.
- [Frontend landing page](LMS-frontend/index.html), [login script](LMS-frontend/auth/login/login.js), [dashboard](LMS-frontend/pages/dashboard/index.html), and [instructor dashboard](LMS-frontend/pages/instructor/dashboard/dashboard.html) — frontend pages, CDN libraries, local API URL, and `localStorage` use.
- [Build workflow](.github/workflows/build.yml), [context test](LMS-backend/src/test/java/com/project/LMS/LmsApplicationTests.java), and [LICENSE](LICENSE) — automated checks and license.
