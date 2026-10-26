# CI / CD

## Principles

1. **Always buildable** — every push/PR to `main` runs assemble + tests.
2. **Coverage gate** — JaCoCo line coverage **≥ 80%** or the job fails.
3. **Stores only after green CI** — Play / RuStore deploy workflows need successful `ci` on the same commit.
4. **Same commands locally and in CI** — see `.cursor/rules/gradle-ci-parity.mdc`.

## Workflows

| Workflow | Trigger | Actions | Status |
|----------|---------|---------|--------|
| `ci.yml` | push/PR → `main` | `./gradlew check assembleDebug`; UI job: emulator API 31; **Telegram notify** after both | Done |
| `deploy-play.yml` | after green CI / `workflow_dispatch` | bump/version → sign AAB → Play internal + Telegram | Pending ops task |
| `deploy-rustore.yml` | `workflow_dispatch` | upload to RuStore | Pending ops task |

Telegram details: [TELEGRAM_NOTIFY.md](TELEGRAM_NOTIFY.md).

## Local pre-push

```bash
./gradlew check
./gradlew assembleRelease   # when signing configured
```

## Secrets (GitHub Actions)

| Secret | Used by |
|--------|---------|
| `TELEGRAM_BOT_TOKEN` | CI notify (+ Play when wired) — **required for chat alerts** |
| `TELEGRAM_CHAT_ID` | same |
| Play service account JSON | `deploy-play` |
| Upload keystore + passwords | signing |
| RuStore API token | `deploy-rustore` |

Never commit these values. Checklist: [TELEGRAM_NOTIFY.md](TELEGRAM_NOTIFY.md), [PLAY_CONSOLE.md](PLAY_CONSOLE.md), [RUSTORE.md](RUSTORE.md).

## Coverage

- Tool: JaCoCo
- Scope: `:app` + library modules with production code
- Threshold: `MIN_COVERAGE=80` (property in Gradle + CI env)
- Exclusions: generated Hilt/databinding, `BuildConfig`, pure Compose preview stubs (document any exclusion in the skeleton task)

## Artifacts

- Debug/Release APK/AAB as CI artifacts on failure/success for debugging
- Mapped with SemVer in [RELEASES.md](RELEASES.md) after first release
