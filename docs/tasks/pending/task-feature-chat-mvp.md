# Task: Chat text + history MVP

**Status:** pending  
**Depends on:** sessions

## Goal

Load message history, send text compose, show user/assistant bubbles (stub + HTTP). SSE can follow in a separate task.

## TDD

- Unit: message models, pagination cursor if present, send/ack
- UI: open demo session, send, see stub reply

## Acceptance

- [ ] History loads; send works against stub and MockWebServer
- [ ] Accessibility/test tags stable
