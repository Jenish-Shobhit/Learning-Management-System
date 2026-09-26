# Backend

This directory contains the Java 21 and Spring Boot 3.4.5 API. It uses Spring Data JPA with the MySQL datasource configured in `src/main/resources/application.properties`.

For database setup, local run commands, API entry points, and security limitations, see the [repository README](../README.md).

From this directory, run `./mvnw spring-boot:run` after setting `DB_USERNAME` and `DB_PASSWORD`. Run `./mvnw verify` to execute the context test against a reachable MySQL database.
