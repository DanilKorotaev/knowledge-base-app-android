# Task: Telegram CI notifications

**Status:** completed  
**Completed:** 2026-10-26

## Summary

- `scripts/ci/telegram_notify.py` — events `tests` and `play` (Play wired later)
- `ci.yml` job `notify-telegram` after unit + UI (`if: always()`)
- Docs: `docs/TELEGRAM_NOTIFY.md`
- Secrets: `TELEGRAM_BOT_TOKEN`, `TELEGRAM_CHAT_ID` (same as iOS)

## Owner follow-up

Add the two secrets to the Android GitHub repo if not inherited from the org.
