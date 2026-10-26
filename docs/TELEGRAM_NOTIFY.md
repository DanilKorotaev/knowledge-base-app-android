# Telegram CI notifications

Parity with iOS: after CI finishes (unit + UI), a bot message goes to the configured chat.

## Secrets (GitHub → Settings → Secrets and variables → Actions)

| Secret | Meaning |
|--------|---------|
| `TELEGRAM_BOT_TOKEN` | Bot token from @BotFather |
| `TELEGRAM_CHAT_ID` | Chat / channel id |

Same names as `knowledge-base-app-ios`. Copy values into **this** repo if they are not organization-wide secrets.

Without secrets the notify step **skips** (no hard fail).

## What is sent

- Combined unit + UI result (`scripts/ci/telegram_notify.py tests`)
- JaCoCo LINE coverage % when reports are present
- Failed test names from JUnit XML (up to 25)
- Link to the workflow run

Play uploads will use `telegram_notify.py play` from `deploy-play.yml` (ops task).

## Local dry-run

```bash
TELEGRAM_NOTIFY_DISABLED=1 python3 scripts/ci/telegram_notify.py tests \
  --outcome success --ui-outcome skipped
```

With a real bot (do not commit tokens):

```bash
export TELEGRAM_BOT_TOKEN=…
export TELEGRAM_CHAT_ID=…
python3 scripts/ci/telegram_notify.py tests --outcome success --ui-outcome success
```

## Disable

`TELEGRAM_NOTIFY_DISABLED=1` — skip send without error.
