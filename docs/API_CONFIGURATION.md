# API configuration

## Runtime (production path)

Same product model as iOS:

1. User enters **KB App API base URL** (onboarding / Settings).
2. Login / register → **JWT** stored in encrypted prefs / Keystore-backed store.
3. All API calls: `Authorization: Bearer <token>` plus client version headers (when implemented).

No bake-in of real hosts or tokens in the APK committed to git.

## Tooling / UI tests

| Variable | Role |
|----------|------|
| `KBAPP_API_BASE_URL` | Optional override for instrumentation |
| `KBAPP_AUTH_TOKEN` | Optional; prefer stub mode for CI UI tests |

UI tests should default to **stub repositories** (no network), like iOS `-UITesting`.

## Client metadata (planned)

Align with iOS when implementing:

- `X-KB-App-Version` ← root `VERSION`
- `X-KB-App-Build` ← CI run number / `versionCode`
- Platform identifier so the server can distinguish Android vs iOS

## Contract source of truth

Do not fork paths/JSON. Sync from:

- `knowledge-base-app-ios/docs/KB_APP_API_CONTRACT.md`
- OpenAPI under iOS/bot docs

Android may keep a **vendored copy** under `docs/` only if linked and updated in the same change as the canonical file (see pending sync task).
