---
name: sports_booking_rules
description: Hard constraints and actionable rules for the SportsBookingSystem Java console project.
---

# Copilot / Cursor Instructions — SportsBookingSystem

This document provides **hard constraints and actionable rules** for AI coding agents working on the **SportsBookingSystem** Java console project.

> ⚠️ This is a **contract**, not suggestions. AI must follow these rules even if alternative designs seem better.

---

## Big Picture Architecture

- **Type:** Single-process, console-based Java application.

- **Architecture:** Layered Architecture (NOT Web MVC).

- **Layers & Responsibilities:**
  - **DAO (`src/daos/*`)**: Persistence only (CSV file I/O).
  - **Service (`src/services/*`)**: Business rules, validation, reporting.
  - **View (`src/views/*`)**: Console input/output only.
  - **Controller (`src/controllers/MainController`)**: Application flow & menu routing.

- **Application Flow:**
  - DAOs load data once at startup
  - Services operate on in-memory lists
  - Controller manages menu loops
  - DAOs save data on exit

- **Entry point:** `src/Main.java`
  - Composition order **must not change**: DAO → Service → View → Controller

---

## Applied Design Patterns

- **Layered Architecture** (DAO / Service / View / Controller)
- **Application Controller Pattern** (menu & flow routing)
- **Template Method** (`TextFileHandler<T>`: `load()` / `save()` fixed, `parseLine()` as hook)
- **Facade Pattern** (Service layer hides DAO & business complexity)
- **Middleware / Chain of Responsibility** (`FacilityMiddleware` validation pipeline)
- **Strategy-ready Persistence** (DAO can be swapped via `IFileService<T>`)
- **Centralized Error Handling** (`ErrorHandler.handle(...)`)

❌ Do NOT refactor or replace these patterns unless explicitly requested.

---

## Tech Stack & Constraints

- **Java Version:** Java 8 (do NOT use features newer than this: no `var`, no `record`, no switch expressions).

- **Build Tool:** Ant (NetBeans-style project).

- **Libraries:** Standard Java SDK ONLY.
  - ❌ No Lombok
  - ❌ No Jackson
  - ❌ No Apache Commons
  - ❌ No external CSV parsers

- **Testing:**
  - No unit test framework is configured.
  - If testing is requested, generate a `public static void main` test harness in a separate class.

---

## Build & Run

From project root (contains `build.xml`):

- `ant` or `ant default` — build project
- `ant run` — run Main class

Opening the project in NetBeans will reuse existing `nbproject` configuration.

---

## Key Files & Reference Points

- **Main:** `src/Main.java` — object wiring and startup lifecycle

- **Controller:** `src/controllers/MainController.java`
  - Menu routing
  - Wrapped by `ErrorHandler.handle(...)`

- **Services:**
  - `BookingService`
  - `FacilityService`

- **DAOs:**
  - `BookingDAO`, `FacilityDAO`
  - Extend `TextFileHandler<T>`
  - Must implement `parseLine(String)` defensively

- **Utilities:**
  - `TextFileHandler.java` — generic CSV load/save
  - `Menu.java`, `Inputter.java` — console UI helpers

---

## Data & File Formats

- **text files (defined in `AppConstants`):**
  - `Homestays2.txt`

- **Storage Date Format (CSV):**
  - `ISO_LOCAL_DATE_TIME` (e.g. `2026-01-29T14:30:00`)

- **UI Date Format:**
  - `dd/MM/yyyy HH:mm`
  - Defined in `AppConstants.DATE_TIME_FORMATTER`

- **Persistence Rules:**
  - `FacilityDAO` -> CSV (`TextFileHandler`)
  - `BookingDAO` -> CSV (`TextFileHandler`) - **USER REQUEST: CSV ONLY**
  - One object per line for CSV.
  - Malformed lines must return `null` from `parseLine()`

⚠️ If `toString()` format changes, **DAO parsing must be updated accordingly**.

---

## Identity (ID) Generation Rules

- **ID Type:** Integer or String (project-defined)

- **Strategy:**
  - When creating a new entity:
    - If the current list is empty: set ID = 1
    - Else: find the current maximum ID in the loaded list and increment by 1

- **Immutability:**
  - IDs must never be changed after assignment

- **Responsibility:**
  - ID generation logic lives in **Service or DAO**, not View

---

## Critical Business Rules & Validation

### Booking Constraints

- Start time **must be before** end time
- Cannot book in the past
- No time overlap for the same facility

**Overlap condition:**

```
(StartA < EndB) && (EndA > StartB)
```

### Validation Responsibilities

- **View level:**
  - Input format validation only
  - Correct date format, non-empty strings

- **Service level (final authority):**
  - Business rules
  - Conflict detection
  - Existence checks

- **Middleware:**
  - Throw exceptions only
  - Never print to console

---

## Input Handling Rules

- Always use `Inputter` static methods:
  - `Inputter.getString(...)`
  - `Inputter.getInt(...)`

- These methods:
  - Handle `try-catch`
  - Loop until valid input

❌ Do NOT:

- Create new `Scanner` instances
- Write raw input loops in Views or Controllers

---

## Coding Standards

- **Naming:**
  - Classes / Interfaces: `PascalCase`
  - Methods / variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`

- **Encapsulation:**
  - All fields must be `private`
  - Access via getters/setters

- **Access Modifiers:**
  - Always explicitly declare (`private`, `public`, etc.)

- **Documentation:**
  - Add Javadoc for all **public Service methods**
  - Include parameters, return values, and thrown exceptions

- **Logging:**
  - No debug prints in DAO or Service
  - Only Views may print user-facing output

---

## Error Handling

- Controller methods may throw exceptions

- All controller calls must be wrapped by:
  - `ErrorHandler.handle(() -> controller.method())`

- Do not swallow exceptions silently

---

## Persistence Lifecycle Rules

- `DAO.load()`:
  - Called **once at application startup**

- `DAO.save()`:
  - Called when user exits application
  - Or after major mutating operations (if explicitly designed)

❌ Do NOT auto-save after every small operation unless specified

---

## Safe Edit Rules for AI

- Prefer modifying existing classes over creating new ones
- Avoid breaking changes to public method signatures
- If a signature changes, update the **entire call chain**
- Do not introduce new packages or frameworks
- Do not refactor architecture unless explicitly requested

---

## What Is Explicitly Out of Scope

- Web MVC
- REST APIs
- Databases / ORM
- Multithreading
- Framework migration

---

## 🚫 Common AI Anti-Patterns (STRICTLY FORBIDDEN)

Do NOT commit these common mistakes. Verify your code against this list before outputting:

1. **The "Scanner Trap":**
   - ❌ BAD: Creating `new Scanner(System.in)` inside methods. This often leads to `NoSuchElementException` or accidentally closes the standard input stream.
   - ✅ GOOD: Always reuse the existing `Inputter` static methods (which handle loops via `wrapRetry`).

2. **The "Date/Time Hallucination":**
   - ❌ BAD: Using `java.util.Date` or `java.sql.Date`.
   - ❌ BAD: Hardcoding date formats like `yyyy-MM-dd` when the project requires `dd/MM/yyyy`.
   - ✅ GOOD: Use `java.time.LocalDateTime` and date/time constants defined in `AppConstants`.

3. **The "Silent Swallower":**
   - ❌ BAD: Empty catch blocks: `catch (Exception e) {}`.
   - ❌ BAD: Printing stack traces directly to the user console: `e.printStackTrace()`.
   - ✅ GOOD: Throw meaningful exceptions and let `MainController` / `ErrorHandler` handle user-facing messages.

4. **The "Over-Engineer":**
   - ❌ BAD: Introducing unnecessary generic abstractions (e.g., `IService<T>`) without real benefit.
   - ❌ BAD: Using complex Java Streams for business logic that reduces readability.
   - ❌ BAD: Using Java 8 Streams for simple iterations (verbose and harder to debug).
   - ✅ GOOD: Prefer standard `for` or `foreach` loops for readability in Java 8.

5. **The "State Amnesia":**
   - ❌ BAD: Re-loading CSV data inside loops, services, or controller methods.
   - ✅ GOOD: Load ONCE at startup, operate on in-memory `List<T>`, save ONCE at exit.

---

## Project Structure Reference

```
src/
├── controllers/   # MainController.java
├── daos/          # BookingDAO.java, FacilityDAO.java
├── models/        # Domain POJOs
├── services/      # Business Logic
├── utils/         # TextFileHandler.java, Inputter.java, AppConstants.java
├── views/         # Console UI classes
└── Main.java      # Application Entry Point
```

---

## When in Doubt

- Follow this document over personal preference
- Choose the simplest solution that respects current architecture
- Ask for clarification only if requirements conflict
