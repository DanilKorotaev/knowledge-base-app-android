# Coding Standards

Mandatory for all new code in this repository.

## 1) Interface-first design

- Every service or repository is an **interface** (or abstract API) in `domain` / API layer.
- Production code depends on abstractions; inject via Hilt.
- Provide a **stub** implementation for UI tests and “no URL” demo mode.

## 2) Test-driven development (TDD)

Order for every behavior change:

1. Write or extend a **failing** unit/UI test that describes the behavior.
2. Implement the minimal production code to pass.
3. Refactor with tests green.

Agents: see `.cursor/rules/tdd-first.mdc`. Do not ship production-only PRs without tests covering the new paths.

## 3) Test coverage

- New production behavior **must** include unit tests in the same change.
- Prefer fakes/mocks (MockK) and FakeWebServer / OkHttp MockWebServer for HTTP.
- Non-trivial Compose flows get UI tests with stub runtime (see `docs/testing/UI.md`).
- **CI gate:** line coverage **≥ 80%** (`MIN_COVERAGE=80` in Gradle/JaCoCo and CI). Below the threshold, CI fails and store deploy is skipped.
- Do **not** lower `MIN_COVERAGE` without an explicit owner decision.

## 4) Definition of done

1. Failing test written first (or clearly extended), then code.
2. Interfaces updated where applicable; stubs kept in sync.
3. Local CI-parity command green: `./gradlew check` (unit + coverage ≥ 80%).
4. UI smoke tests updated when user-visible flows change: `./gradlew connectedCheck` / documented CI UI job when emulator job exists.
5. Docs or `docs/todo.md` updated if scope/config changed. HTTP surface → sync with canonical KB App API contract.

## 5) Project policy

- No secrets, tokens, or personal data in git.
- Configuration via onboarding UI, encrypted storage, gitignored local files, or CI secrets.
- Prefer Conventional Commits: `feat:`, `fix:`, `docs:`, `test:`, `chore:`.
