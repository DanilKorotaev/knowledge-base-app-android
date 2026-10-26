# Task: Composer + attachments

**Status:** pending  
**Depends on:** chat MVP

## Goal

Telegram-like composer: text, gallery/file pick, preview chips, multipart upload per contract.

## TDD

- Unit: attachment validation/limits, multipart builder
- UI: attach → send with stub

## Acceptance

- [ ] Permissions flow Android 12+ correct
- [ ] Failures show retry without losing draft (align iOS lessons)
