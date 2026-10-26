# Task: Chat SSE streaming

**Status:** pending  
**Depends on:** chat MVP

## Goal

Stream assistant reply via `text/event-stream` (OkHttp EventSource), UX feedback similar to iOS (typing / deltas).

## TDD

- Unit: SSE parser + ViewModel streaming states (failing tests first)
- UI: stub stream produces growing bubble

## Acceptance

- [ ] Parser covered for `delta` / `done` / error events
- [ ] Resume/cancel behavior documented; tests for cancel
