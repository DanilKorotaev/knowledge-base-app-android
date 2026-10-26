# RuStore (optional)

Secondary Android store for RU distribution. **Not blocking** Google Play MVP.

## When to enable

- Play pipeline already uploads internal/closed builds successfully.
- Owner decides RuStore is needed for reach / payments / discoverability.

## Planned setup

1. RuStore Console app with the **same** `applicationId` if policy allows, or document a fork package if required.
2. API token / key → GitHub secret `RUSTORE_TOKEN` (name finalized in ops task).
3. Workflow `deploy-rustore.yml` — primarily `workflow_dispatch`; optional auto after Play internal.
4. Signing: reuse Play upload keystore if RuStore accepts the same cert chain; otherwise document a second key.

## Differences vs Play

| Topic | Note |
|-------|------|
| Review / listing | Separate console copy, screenshots, privacy |
| Push | FCM may need RuStore push dual-path later |
| CI | Must not weaken coverage gate |

## Task

See `docs/tasks/pending/task-ops-rustore-optional.md`.
