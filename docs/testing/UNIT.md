# Unit tests

## Tools

- JUnit 5
- MockK
- Turbine (Flow)
- Truth (assertions)
- OkHttp MockWebServer for HTTP

## What must be covered

- ViewModels / use cases / repositories
- JSON/DTO mapping and error parsing (`error.code`, …)
- SSE event parser
- Offline merge / cache rules (when present)

## Run

```bash
./gradlew test
./gradlew check   # includes JaCoCo ≥ 80%
```

## TDD

Write the failing unit test **before** production code. See `.cursor/rules/tdd-first.mdc`.
