# Setup

## Prerequisites

1. **Android Studio** (current stable) with Android SDK **36**, build-tools, and an emulator image **API 31+** (match minSdk).
2. **JDK 17** (Studio embedded JDK is fine).
3. Physical device optional: Android **12+** for on-device checks (owner baseline).

## Clone and sync

1. Clone [knowledge-base-app-android](https://github.com/DanilKorotaev/knowledge-base-app-android) (or open the vault copy).
2. Create `local.properties` (gitignored) with your SDK path, e.g.:

   ```properties
   sdk.dir=/Users/YOU/Library/Android/sdk
   ```

   Homebrew cmdline-tools default: `sdk.dir=/opt/homebrew/share/android-commandlinetools`

3. Open the repo root in Android Studio → Gradle sync.
4. Run configuration: **app** → Debug on emulator or device.

Current shell shows a placeholder “Knowledge Base” title and the stub **Demo session**. Auth / real API URL come in later tasks.

## CI-parity commands

```bash
./gradlew check          # unit tests + JaCoCo ≥ 80% + lint
./gradlew assembleDebug  # always-buildable APK
```

Instrumented Compose UI tests (needs emulator/device):

```bash
./gradlew connectedDebugAndroidTest
```

## Configuration and secrets

See [API_CONFIGURATION.md](API_CONFIGURATION.md).

- Do **not** commit real API hosts or session tokens.
- Until auth lands, Hilt provides `StubKnowledgeBaseApiClient`.
- Optional instrumentation overrides via `KBAPP_*` for UI tests later.

## Signing (release)

- Debug: default Android debug keystore.
- Release / Play: upload keystore **outside git**; paths via `keystore.properties` (gitignored) or CI secrets. Documented in [PLAY_CONSOLE.md](PLAY_CONSOLE.md).

## Microphone / notifications

Manifest permissions for mic (voice) and notifications (FCM) are added with the corresponding feature tasks — not before tests for those features exist.
