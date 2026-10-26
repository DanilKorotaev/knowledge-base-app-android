# KnowledgeBaseApp (Android) — documentation

## Contents

- [TECH_STACK.md](TECH_STACK.md) — locked technology choices
- [SETUP.md](SETUP.md) — Android Studio, SDK, device
- [API_CONFIGURATION.md](API_CONFIGURATION.md) — URL/token / UI-test overrides
- [DEVELOPMENT.md](DEVELOPMENT.md) — Git flow, naming, daily loop
- [ARCHITECTURE.md](ARCHITECTURE.md) — modules and API boundary
- [CODING_STANDARDS.md](CODING_STANDARDS.md) — interface-first + TDD + coverage ≥ 80%
- [CI_CD.md](CI_CD.md) — GitHub Actions, gates, secrets
- [RELEASE_PROCESS.md](RELEASE_PROCESS.md) — SemVer, tags `android/v*`
- [RELEASES.md](RELEASES.md) — versionCode ↔ store mapping
- [PLAY_CONSOLE.md](PLAY_CONSOLE.md) — Google Play setup
- [RUSTORE.md](RUSTORE.md) — optional second store
- [todo.md](todo.md) — active tasks
- [completed.md](completed.md) — completed tasks

## Testing

- [testing/UNIT.md](testing/UNIT.md)
- [testing/UI.md](testing/UI.md)
- [testing/E2E.md](testing/E2E.md)

## Task folders

- `docs/tasks/pending/` — planned work
- `docs/tasks/completed/` — finished task specs

## Agent rules

- `.cursor/rules/tdd-first.mdc`
- `.cursor/rules/ci-coverage-gate.mdc`
- `.cursor/rules/gradle-ci-parity.mdc`

## Canonical API contract

Prefer iOS / bot copies until Android vendors a synced file:

- `knowledge-base-app-ios/docs/KB_APP_API_CONTRACT.md`
