# Learning Management System

This repository contains a Spring Boot API in `LMS-backend/` and a static web frontend in `LMS-frontend/`. The code covers users, courses, enrollments and progress, quizzes, and questions. Student and instructor pages call the API directly from the browser.

## Stack and structure

- **Backend:** Java 21, Spring Boot 3.4.5, Maven, Spring Web, Spring Data JPA, and Spring Security as a dependency.
- **Layers:** `controller/` exposes HTTP endpoints; `service/` holds application logic; `Repository/` contains Spring Data JPA repositories; `model/` contains JPA entities. `dto/`, `response/`, `exceptions/`, and `security/` hold supporting types and configuration.
- **Persistence:** The configured datasource is MySQL (`jdbc:mysql://localhost:3306/lmsdb`). Hibernate uses `ddl-auto=update`. Repositories use JPA methods and two native SQL queries for leaderboard and progress tracking.
- **Passwords:** Registration and password updates use BCrypt to hash passwords; login checks them with BCrypt. The separate `/api/users` save endpoint calls the repository directly, so the code does not guarantee hashing through every user-write path.
- **Frontend:** Plain HTML, CSS, and JavaScript, with jQuery, Bootstrap 4, Font Awesome 5, and Chart.js loaded from CDNs. There is no frontend build step in the repository. The landing page is `LMS-frontend/index.html`; `auth/` contains login, signup, and password-change pages; `pages/` contains student and instructor views.

## Run locally

1. Install JDK 21, Maven, MySQL, and Python 3 (or another static HTTP server).
2. Create the database in MySQL:

   ```sql
   CREATE DATABASE lmsdb;
   ```

3. Set the local MySQL username and password in `LMS-backend/src/main/resources/application.properties`. Its datasource URL currently targets `localhost:3306/lmsdb`; the API port is `2025`.
4. Start the backend from the repository root:

   ```sh
   cd LMS-backend
   mvn spring-boot:run
   ```

5. In a second terminal, serve the frontend from its own directory:

   ```sh
   cd LMS-frontend
   python3 -m http.server 5500 --bind 127.0.0.1
   ```

6. Open `http://127.0.0.1:5500/`. The frontend's API URLs point to `http://localhost:2025`, and the backend CORS mapping allows `http://127.0.0.1:5500`. The CDN assets also require network access.

The checked-in `mvnw` scripts reference `.mvn/wrapper/maven-wrapper.properties`, which is absent, so use an installed Maven executable as shown above.

## Limitations

- `SecurityConfig` uses `anyRequest().permitAll()` and disables CSRF, form login, and HTTP Basic. Spring Security is present, but the API routes are not protected by it.
- `/login` returns a user ID and role after a password check; it does not issue a JWT or establish an authenticated session. The frontend stores the user ID in `localStorage`.
- `/updatePassword` accepts a user ID and new password without an authentication check in the controller. Do not treat the current password-change flow as a secure recovery mechanism.
- The leaderboard native query uses `ROWNUM`, although the configured datasource is MySQL. That query may fail on MySQL.

## Verified in

- `LMS-backend/pom.xml` — Java version, Spring Boot version, Maven dependencies, and backend stack.
- `LMS-backend/src/main/java/com/project/LMS/controller/`, `service/`, `Repository/`, `model/`, `dto/`, `response/`, and `exceptions/` — layered structure and implemented API areas.
- `LMS-backend/src/main/resources/application.properties` — MySQL URL, driver, Hibernate setting, and port.
- `LMS-backend/src/main/java/com/project/LMS/Repository/EnrollmentRepository.java` — native SQL queries and `ROWNUM`.
- `LMS-backend/src/main/java/com/project/LMS/service/UserService.java`, `controller/loginController.java`, and `controller/UserController.java` — BCrypt hashing, password checking, login response, and user-write paths.
- `LMS-backend/src/main/java/com/project/LMS/security/SecurityConfig.java` — `permitAll`, disabled security features, and allowed frontend origin.
- `LMS-frontend/index.html`, `LMS-frontend/pages/dashboard/index.html`, and `LMS-frontend/auth/login/login.html` — static pages and CDN libraries; `LMS-frontend/auth/login/login.js` — API URL and `localStorage` use.
- `LMS-backend/mvnw` and the absence of `LMS-backend/.mvn/wrapper/maven-wrapper.properties` — why the documented command uses installed Maven.
