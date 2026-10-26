# CI / CD

## Principles

1. **Always buildable** — every push/PR to `main` runs assemble + tests.
2. **Coverage gate** — JaCoCo line coverage **≥ 80%** or the job fails.
3. **Stores only after green CI** — Play / RuStore deploy workflows need successful `ci` on the same commit.
4. **Same commands locally and in CI** — see `.cursor/rules/gradle-ci-parity.mdc`.

## Workflows

| Workflow | Trigger | Actions | Status |
|----------|---------|---------|--------|
| `ci.yml` | push/PR → `main` | `./gradlew check assembleDebug`; UI job: emulator API 31 + `connectedDebugAndroidTest` | Done |
| `deploy-play.yml` | after green CI / `workflow_dispatch` | bump/version → sign AAB → Play internal | Pending ops task |
| `deploy-rustore.yml` | `workflow_dispatch` | upload to RuStore | Pending ops task |

## Local pre-push

```bash
./gradlew check
./gradlew assembleRelease   # when signing configured
```

## Secrets (GitHub Actions — names TBD in ops tasks)

| Secret | Used by |
|--------|---------|
| Play service account JSON | `deploy-play` |
| Upload keystore + passwords | signing |
| RuStore API token | `deploy-rustore` |
| Telegram notify (optional) | release announce |

Never commit these values. Checklist lives in [PLAY_CONSOLE.md](PLAY_CONSOLE.md) and [RUSTORE.md](RUSTORE.md).

## Coverage

- Tool: JaCoCo
- Scope: `:app` + library modules with production code
- Threshold: `MIN_COVERAGE=80` (property in Gradle + CI env)
- Exclusions: generated Hilt/databinding, `BuildConfig`, pure Compose preview stubs (document any exclusion in the skeleton task)

## Artifacts

- Debug/Release APK/AAB as CI artifacts on failure/success for debugging
- Mapped with SemVer in [RELEASES.md](RELEASES.md) after first release
