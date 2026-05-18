# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

**Escala de Plantões** — a Spring Boot REST API + Vue 3 frontend for managing hospital shift scheduling. Handles professional registration, shift (plantão) creation/deletion, and weekly schedule listing.

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

H2 runs in **file mode** in development (`jdbc:h2:file:./escaladb`, `ddl-auto: update`). H2 console at `http://localhost:8080/h2-console` (user: `sa`, no password). Tests override this with in-memory H2 via `src/test/resources/application.yaml`.

All REST endpoints are prefixed with `/api` (e.g. `POST /api/professionals`, `GET /api/escala`).

## Frontend

```bash
cd frontend
npm install
npm run dev      # dev server at http://localhost:5173
npm run build    # type-check + production build
```

Vue 3 + Vite + TypeScript. Views: `ProfessionalsView`, `PlantaoView`, `EscalaView`. Uses `axios` for API calls and `vue-router` for navigation. The frontend calls the backend at the same host on port 8080 (no proxy config — run backend separately).

## Seed

```powershell
# With backend running at localhost:8080
.\seed\seed.ps1
```

Registers 7 fictitious professionals and 12 plantões across the next 7 days. Safe to run multiple times — registration numbers are suffixed with a timestamp.

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

**Instance variable names mirror their class name**, with the first letter lowercased. Example: `private final RegisterPlantaoUseCase registerPlantaoUseCase;`. Never abbreviate or drop words from the class name.

### Adding a new feature

1. Create a new top-level package under `demo/` (e.g., `shift/`).
2. Define domain entities/enums in `domain/`.
3. Add a `JpaRepository` in `infrastructure/`.
4. Add a `@Service` in `service/` that delegates to the repository.
5. Create one use case class per operation in `usecase/`, annotated with `@Component`.
6. Expose endpoints in `controller/` using request/response records.

## Feature: Professional

### controller/
- `ProfessionalController` — endpoints REST do profissional (`POST /api/professionals`, `GET /api/professionals`, `GET /api/professionals/category`)
- `dto/ProfessionalRequestDTO` — record de entrada para cadastro do profissional
- `dto/ProfessionalRegistrationRequestDTO` — record de entrada para os dados de registro (embutido no request de cadastro)
- `dto/ProfessionalResponseDTO` — record de saída com factory `from(Professional)`
- `dto/ProfessionalRegistrationResponseDTO` — record de saída dos dados de registro (embutido no response)
- `dto/ProfessionalFilterRequestDTO` — record de filtro para `GET /professionals/category` (query params via `@ModelAttribute`)

### domain/
- `Professional` — entidade JPA principal; possui `@OneToOne(cascade = ALL, orphanRemoval = true)` para `ProfessionalRegistration`
- `ProfessionalRegistration` — entidade JPA com dados de registro (CRM, especialidade, categoria, carga horária)
- `ProfessionalValidator` — lógica de validação de domínio (regras de negócio)

### infrastructure/
- `ProfessionalRepository` — `JpaRepository<Professional, Long>` com query de busca por categoria e carga horária

### service/
- `ProfessionalService` — `@Service` que delega ao `ProfessionalRepository`; recebe e retorna entidades, nunca DTOs

### usecase/
- `RegisterProfessionalUseCase` — cadastra um novo profissional
- `ListProfessionalsUseCase` — lista todos os profissionais
- `ListProfessionalsByCategoryUseCase` — lista profissionais filtrando por categoria/carga horária
- `validator/ProfessionalValidator` — validações reutilizadas entre use cases

---

## Feature: Escala

### controller/
- `EscalaController` — `GET /api/escala?data=YYYY-MM-DD` — retorna os plantões dos 7 dias a partir da data informada

### usecase/
- `ListEscalaUseCase` — retorna os plantões dos 7 dias a partir de `dataInicio` (inclusive), agrupados por profissional

---

## Feature: Plantao

### controller/
- `PlantaoController` — endpoints REST do plantão (`POST /api/plantoes`, `DELETE /api/plantoes/{id}`)
- `dto/PlantaoRequestDTO` — record de entrada: `professionalId` (Long), `data` (LocalDate), `turno` (Turno)
- `dto/PlantaoResponseDTO` — record de saída com factory `from(Plantao)`

### domain/
- `Plantao` — entidade JPA; `@ManyToOne` para `Professional`, `data` (LocalDate), `turno` (Turno enum)
- `Turno` — enum com valores `MANHA`, `TARDE`, `NOITE`

### infrastructure/
- `PlantaoRepository` — `JpaRepository<Plantao, Long>`

### service/
- `PlantaoService` — `@Service` que delega ao `PlantaoRepository`

### usecase/
- `RegisterPlantaoUseCase` — cria um plantão; busca o `Professional` via `ProfessionalService.findById()`
- `DeletePlantaoUseCase` — exclui um plantão pelo id; lança `IllegalArgumentException` se não existir

---

## Tech stack

**Backend:** Java 21, Spring Boot 4.x, Spring Data JPA, H2, Lombok, JUnit 5

**Frontend:** Vue 3, Vite 8, TypeScript, Vue Router 5, Pinia, Axios
