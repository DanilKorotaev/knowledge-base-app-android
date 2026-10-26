# Task: KB App API contract sync

**Status:** pending  
**Priority:** P1 — before real HTTP client

## Goal

Android consumes the **same** contract as iOS/bot without drift.

## Deliverables

- Decision: vendored copy under `docs/KB_APP_API_CONTRACT.md` + OpenAPI **or** documented pointer-only + CI check
- Client error parsing aligned with `error.code` / `message` / `detail`
- Version headers plan (`X-KB-App-Version`, build, platform=android)

## Acceptance

- [ ] Sync process written in `API_CONFIGURATION.md` / `ARCHITECTURE.md`
- [ ] First Retrofit interfaces match canonical paths
