# Knowledge Base (Android)

Native **Kotlin + Jetpack Compose** client for the personal knowledge base stack: same PostgreSQL sessions and processing pipeline as the Telegram bot and iOS app, via **KB App API** (FastAPI).

**Repository:** [github.com/DanilKorotaev/knowledge-base-app-android](https://github.com/DanilKorotaev/knowledge-base-app-android)  

**iOS sibling:** [knowledge-base-app-ios](https://github.com/DanilKorotaev/knowledge-base-app-ios)  
**API:** `knowledge-base-bot/kb_app_api/`

## Goals

- Feature and UX parity with the iOS KB App where platform allows (chat, sessions, voice, attachments, Overview/Boards, Structured UI, offline, push).
- Always buildable on CI; unit + UI tests; **line coverage ≥ 80%**.
- **TDD:** write failing tests first, then production code.
- Publish to **Google Play** (primary); **RuStore** documented as optional second store.

## Requirements (planned)

| Item | Choice |
|------|--------|
| Language / UI | Kotlin 2.x, Jetpack Compose, Material 3 |
| minSdk | **31** (Android 12) — test device baseline |
| targetSdk / compileSdk | **36** (Android 16) — Play requirement for new apps (2026) |
| IDE | Android Studio (current stable) |
| JDK | 17+ |

Full decisions: [docs/TECH_STACK.md](docs/TECH_STACK.md).

## Status

**Documentation + agent rules + task backlog** are in place. Application skeleton (Gradle modules, CI workflows, first screens) is tracked as pending tasks — start with [docs/todo.md](docs/todo.md).

## Documentation

| Document | Description |
|----------|-------------|
| [docs/README.md](docs/README.md) | Documentation index |
| [docs/TECH_STACK.md](docs/TECH_STACK.md) | Locked technology choices |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Modules and API boundary |
| [docs/CODING_STANDARDS.md](docs/CODING_STANDARDS.md) | Interface-first, TDD, coverage |
| [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) | Daily workflow and Git |
| [docs/SETUP.md](docs/SETUP.md) | Android Studio / device setup |
| [docs/CI_CD.md](docs/CI_CD.md) | GitHub Actions, stores |
| [docs/RELEASE_PROCESS.md](docs/RELEASE_PROCESS.md) | SemVer, tags, changelog |
| [docs/todo.md](docs/todo.md) | Active tasks |

## Agent rules

Mandatory for Cursor agents in this repo:

- `.cursor/rules/tdd-first.mdc` — tests before production code
- `.cursor/rules/ci-coverage-gate.mdc` — coverage ≥ 80%
- `.cursor/rules/gradle-ci-parity.mdc` — same Gradle entry points as CI

## Related

- Vault plan: `Документация/Android-приложение базы знаний/`
- API contract (canonical): `knowledge-base-app-ios/docs/KB_APP_API_CONTRACT.md` (+ OpenAPI in bot/iOS)

## License

[MIT](LICENSE)
