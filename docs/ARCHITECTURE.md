# Architecture

## Goal

Android client to the **same** knowledge base infrastructure as Telegram and iOS:

- Shared **PostgreSQL** (sessions, messages) via KB App API
- Same Cursor / processing services on the server
- Nextcloud WebDAV and Whisper stay **server-side**

The app talks only to **HTTPS APIs**. No Cursor, no DB, no WebDAV from the device.

## Layering (mirrors iOS protocol-first)

```text
ui/          Compose screens, Navigation, theme
presentation ViewModels (state + intents)
domain/      models, use cases, repository interfaces
data/        Retrofit/OkHttp, DTO ↔ domain, local cache, secure storage
di/          Hilt modules
```

Rules:

- UI and ViewModels depend on **interfaces**, not Retrofit types.
- Production vs stub implementations (stub for UI tests / offline demo without URL).
- Constructor / Hilt injection; no business-logic singletons.

## Planned modules (Gradle)

| Module | Role |
|--------|------|
| `:app` | Application, Navigation, Compose UI |
| `:core:network` | OkHttp, Retrofit, SSE helper, auth interceptor |
| `:core:model` | Shared domain models |
| `:core:data` | Repository implementations, DataStore, Room (later) |
| `:feature:*` | Optional feature modules as the app grows |

Skeleton task will create the first cut (`:app` + network/model is enough for MVP).

## Feature map (parity with iOS)

| Area | Notes |
|------|--------|
| Auth | Login/register → JWT in encrypted storage |
| Sessions | List, create, rename, delete, pin |
| Chat | History, pagination, compose text/attachments/voice, SSE streaming |
| Overview / Boards | Structured UI renderer from API JSON |
| Offline | Cache-first + SWR (after online MVP) |
| Push | FCM when reply ready |
| Share-in | Android share sheet → session (later) |
| Health | Via API only if product needs it (no Health Connect until tasked) |

## Backend boundary

- Contract: iOS `KB_APP_API_CONTRACT.md` / OpenAPI (keep Android client aligned in the same change as server updates when paths change).
- Until URL + token: stub repositories seed a demo session (same idea as iOS stubs).

## Constraints (mandatory)

- Interface-first services/repositories
- TDD + tests in the same change
- Coverage ≥ 80% (see `CODING_STANDARDS.md`)
- No secrets in git
