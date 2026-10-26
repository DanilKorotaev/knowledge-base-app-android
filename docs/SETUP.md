# Setup

## Prerequisites

1. **Android Studio** (current stable) with Android SDK **36**, build-tools, and an emulator image **API 31+** (match minSdk).
2. **JDK 17** (Studio embedded JDK is fine).
3. Physical device optional: Android **12+** for on-device checks (owner baseline).

## After Gradle skeleton exists

1. Clone / open `knowledge-base-app-android/` in Android Studio.
2. Let Gradle sync; fix SDK licenses if prompted (`sdkmanager --licenses`).
3. Run configuration: `app` → Debug on emulator or device.
4. First launch: enter KB App API base URL → login/register (same product flow as iOS).

```bash
./gradlew assembleDebug
./gradlew check
```

## Configuration and secrets

See [API_CONFIGURATION.md](API_CONFIGURATION.md).

- Do **not** commit real API hosts or session tokens.
- JWT after login lives in encrypted storage.
- Optional instrumentation overrides via `KBAPP_*` for UI tests only.

## Signing (release)

- Debug: default Android debug keystore.
- Release / Play: upload keystore **outside git**; paths via `keystore.properties` (gitignored) or CI secrets. Documented in [PLAY_CONSOLE.md](PLAY_CONSOLE.md).

## Microphone / notifications

Manifest permissions for mic (voice) and notifications (FCM) are added with the corresponding feature tasks — not before tests for those features exist.
