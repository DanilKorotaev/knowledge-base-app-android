# Task: repository skeleton + CI

**Status:** pending  
**Priority:** P0 — first implementation task

## Goal

Create a buildable Android project that always passes CI with TDD baseline and **coverage ≥ 80%**.

## Deliverables

- Gradle Kotlin DSL multi-module start: `:app`, `:core:model`, `:core:network` (minimal)
- `applicationId` `com.coredan.knowledgebaseapp`, minSdk 31, target/compile 36
- Hilt + Compose empty shell (single placeholder screen) **with tests first**
- JaCoCo + `MIN_COVERAGE=80` failing the build when below
- `.github/workflows/ci.yml`: `./gradlew check` (+ assembleDebug)
- Stub API interface + fake so UI/unit tests need no network
- `CHANGELOG.md` Unreleased section
- Update `docs/SETUP.md` / `DEVELOPMENT.md` with real commands after wiring

## TDD notes

1. Tests for Application/DI smoke and stub client contract before UI polish.
2. Placeholder screen still has a Compose UI test (stub tags).

## Acceptance

- [ ] Fresh clone: Gradle sync + `./gradlew check` green
- [ ] Coverage report ≥ 80%
- [ ] CI workflow present and documented
- [ ] No secrets in repo

## Follow-up

- SemVer/release scripts, Play internal upload
