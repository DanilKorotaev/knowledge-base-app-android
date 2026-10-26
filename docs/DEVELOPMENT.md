# Development

## Layout (target after skeleton)

```text
knowledge-base-app-android/
  app/                 # Compose UI + Application
  core/network/
  core/model/
  core/data/
  gradle/libs.versions.toml
  .github/workflows/
  docs/
  VERSION
```

Until the skeleton task lands, only `docs/`, agent rules, and repo metadata exist.

## Git flow

Trunk-based, same spirit as iOS:

| Branch | Role |
|--------|------|
| `main` | Always releasable; CI must be green |
| `feature/*` or short-lived branches | Optional; merge via PR preferred once remote exists |

- Default remote branch: **`main`**
- Tags: `android/vX.Y.Z` after successful store-bound release
- **No force-push** to `main`
- Commit messages become changelog bullets (see `RELEASE_PROCESS.md`)

## Daily loop

1. Pick a task from `docs/todo.md` / `docs/tasks/pending/`.
2. **Write failing tests** for the next slice.
3. Implement until green.
4. Run CI-parity: `./gradlew check` (and UI job when available).
5. Update task status / move to `completed/` when done.
6. Commit; push to `main` (or open PR) when GitHub remote exists.

## Naming

| Kind | Pattern | Example |
|------|---------|---------|
| Task files | `task-<area>-<slug>.md` | `task-feature-chat-mvp.md` |
| Packages | `com.coredan.knowledgebaseapp…` | `…chat.ChatViewModel` |
| Release tags | `android/vMAJOR.MINOR.PATCH` | `android/v0.1.0` |
| Workflows | `ci.yml`, `deploy-play.yml`, `deploy-rustore.yml` | |

Task areas: `feature`, `ux`, `bug`, `ops`, `infra`, `doc`, `backend` (client sync only).

## CI parity

Agents and humans validate with the **same** Gradle tasks as GitHub Actions. See `.cursor/rules/gradle-ci-parity.mdc` and [CI_CD.md](CI_CD.md).

```bash
./gradlew check          # unit tests + JaCoCo gate ≥ 80%
./gradlew assembleDebug  # always buildable
```

Do not treat “compiles in Android Studio” alone as a pre-push gate.

## Task tracking

- Active index: [todo.md](todo.md)
- Specs: `tasks/pending/` → `tasks/completed/`
- Archive summary: [completed.md](completed.md)
