# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

**Escala de Plantões** — a Spring Boot REST API for managing hospital shift scheduling. Currently handles professional registration and listing; shift scheduling features are planned.

## Commands

```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "escala_plantoes.com.example.demo.DemoApplicationTests"
```

H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:escaladb`, user: `sa`, no password). The schema is created and dropped on each run (`ddl-auto: create-drop`).

## Architecture

The project follows **Clean Architecture** organized by feature package (currently only `professional`). Each feature package contains:

```
demo/
  controller/        # REST controllers + DTOs (request/response records)
  domain/            # JPA entities + enums (no Spring dependencies)
  infrastructure/    # Repository interfaces (Spring Data JPA)
  service/           # @Service — thin layer that delegates to repository
  usecase/           # @Component use case classes, one execute() method each
```

### Key design decisions

**Use cases are `@Component` classes** detected by Spring's component scan. Each has a single `execute()` method.

**Controllers depend only on use cases**, never on services or repositories directly.

**DTOs are Java records** placed inside `controller/dto/`. `ResponseDTO` has a static `from(Entity)` factory. `RequestDTO` is a plain record consumed by the use case.

**Services must not use DTOs.** Services receive and return domain entities only. DTOs are a concern of the controller/use case boundary.

**Domain enums carry behavior.** `WorkSchedule` holds the numeric hours value (`HOURS_20`, `HOURS_30`, `HOURS_40`) — access hours via `getHours()`, not by parsing the name.

### Adding a new feature

1. Create a new top-level package under `demo/` (e.g., `shift/`).
2. Define domain entities/enums in `domain/`.
3. Add a `JpaRepository` in `infrastructure/`.
4. Add a `@Service` in `service/` that delegates to the repository.
5. Create one use case class per operation in `usecase/`, annotated with `@Component`.
6. Expose endpoints in `controller/` using request/response records.

## Tech stack

- Java 21, Spring Boot 4.x
- Spring Data JPA + H2 (in-memory, development only)
- Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor` on entities)
- JUnit 5 via `spring-boot-starter-*-test`
