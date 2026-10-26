# Technology stack (locked)

Decided **2026-10-26**. Change only with an explicit owner decision and a task that updates this file + agent rules.

## Platform

| Item | Choice | Rationale |
|------|--------|-----------|
| Language | **Kotlin 2.x** | Android default; coroutines/Flow |
| UI | **Jetpack Compose** + **Material 3** | Declarative parity with SwiftUI; modern Play UX |
| minSdk | **31** (Android 12) | Owner test device; no legacy OEM surface |
| compileSdk / targetSdk | **36** (Android 16) | Google Play: new apps/updates must target API 36 (from 2026-08-31) |
| Architecture components | ViewModel, Navigation Compose, Lifecycle | Standard Jetpack |
| DI | **Hilt** | Official, testable, Compose-friendly |
| Async | Coroutines + Flow | Mirrors iOS async streams for SSE |
| Networking | **OkHttp** + **Retrofit** + **kotlinx.serialization** | Typed REST; OkHttp `EventSource` for SSE |
| Images | **Coil** | Compose-native |
| Local prefs | DataStore Preferences | Settings / flags |
| Secure token | EncryptedSharedPreferences or Keystore-backed store | Keychain analogue |
| Offline cache (later) | Room + disk cache for attachments | Same ideas as iOS FileOfflineCacheStore |
| Push | Firebase Cloud Messaging (FCM) | Play path; RuStore may need dual registration later |
| Package name | `com.coredan.knowledgebaseapp` | Align with iOS `com.coredan.KnowledgeBaseApp` |

## Explicit non-goals (v1)

- Support below Android 12
- Wear OS / watch companion (iOS Watch is separate; revisit later)
- Kotlin Multiplatform shared module with iOS (keep clients independent; share OpenAPI contract only)
- Flutter / React Native

## Testing

| Layer | Tools | Gate |
|-------|-------|------|
| Unit | JUnit 5, MockK, Turbine, Truth | Required for domain/data/ViewModels |
| UI | Compose UI Test + stub API runtime (`-UITesting` analogue) | Smoke on every PR |
| Coverage | JaCoCo on `:app` (and library modules) | **≥ 80% line** — CI fails below |
| Process | **TDD** — failing test → code → green | Agent rule `tdd-first.mdc` |

## Build & delivery

| Item | Choice |
|------|--------|
| Build | Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`) |
| CI | GitHub Actions (`ubuntu-latest`), JDK 17, emulator for UI tests |
| Always green | `./gradlew check` (unit + coverage) must pass on every PR/`main` |
| Play upload | Fastlane `supply` **or** `gradle-play-publisher` (pick in ops task; document in `PLAY_CONSOLE.md`) |
| RuStore | Optional second pipeline; credentials + API in `RUSTORE.md` |
| Versioning | SemVer in root `VERSION`; tags `android/vX.Y.Z` (see `RELEASE_PROCESS.md`) |

## API contract

Canonical HTTP contract: **iOS** `docs/KB_APP_API_CONTRACT.md` + OpenAPI in bot/iOS. Android **must not** invent divergent paths/JSON. Sync task: `docs/tasks/pending/task-doc-kb-app-api-contract-sync.md`.

## UX / product reference

Mirror iOS tab shell and chat/composer patterns (Chats · Overview · Settings; Telegram-style voice). Platform idioms (Material, system back, notifications) are OK; product flows stay aligned.
