# Task: Overview / Boards renderer

**Status:** pending  
**Depends on:** tab shell, contract

## Goal

`GET /api/boards` list + detail Structured UI nodes (`metric`, `table`, …) with stub demo boards when API 404 — same product idea as iOS.

## TDD

- Unit: JSON node decoder / renderer mapping
- UI: open stub board, assert metrics/table tags

## Acceptance

- [ ] No domain hardcode of vault structure in app (boards from API)
- [ ] Unknown node types fail softly + tested
