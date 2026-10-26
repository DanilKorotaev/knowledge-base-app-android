# Development

## Layout

```text
knowledge-base-app-android/
  app/                      # Compose UI + Hilt Application
  core/model/               # Shared domain models (JVM)
  core/network/             # API interfaces + stub (+ Retrofit later)
  gradle/libs.versions.toml
  gradle/coverage-jvm.gradle.kts
  .github/workflows/ci.yml
  docs/
  VERSION
```

## Git flow

Trunk-based, same spirit as iOS:

| Branch | Role |
|--------|------|
| `main` | Always releasable; CI must be green |
| `feature/*` or short-lived branches | Optional; merge via PR preferred |

- Default remote branch: **`main`**
- Tags: `android/vX.Y.Z` after successful store-bound release
- **No force-push** to `main`
- Commit messages become changelog bullets (see `RELEASE_PROCESS.md`)

## Daily loop

1. Pick a task from `docs/todo.md` / `docs/tasks/pending/`.
2. **Write failing tests** for the next slice.
3. Implement until green.
4. Run CI-parity: `./gradlew check`.
5. Update task status / move to `completed/` when done.
6. Commit and **push** to `origin main`.

## Naming

| Kind | Pattern | Example |
|------|---------|---------|
| Task files | `task-<area>-<slug>.md` | `task-feature-chat-mvp.md` |
| Packages | `com.coredan.knowledgebaseapp…` | `…ui.placeholder.PlaceholderViewModel` |
| Release tags | `android/vMAJOR.MINOR.PATCH` | `android/v0.1.0` |
| Workflows | `ci.yml`, `deploy-play.yml`, `deploy-rustore.yml` | |

Task areas: `feature`, `ux`, `bug`, `ops`, `infra`, `doc`, `backend` (client sync only).

## CI parity

Agents and humans validate with the **same** Gradle tasks as GitHub Actions. See `.cursor/rules/gradle-ci-parity.mdc` and [CI_CD.md](CI_CD.md).

```bash
./gradlew check          # unit tests + JaCoCo gate ≥ 80% + lint
./gradlew assembleDebug  # always buildable
```

Do not treat “compiles in Android Studio” alone as a pre-push gate.

Coverage property: `minCoverage` in `gradle.properties` (default `80`) or env `MIN_COVERAGE`.

## Task tracking

- Active index: [todo.md](todo.md)
- Specs: `tasks/pending/` → `tasks/completed/`
- Archive summary: [completed.md](completed.md)
