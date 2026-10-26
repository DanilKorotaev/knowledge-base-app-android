# E2E (against real / staging API)

Optional layer after stub UI + unit coverage are solid.

## When

- Staging KB App API available (`kbapp-staging…` or current host)
- Auth + sessions + SSE already work in unit/UI with stubs

## Scope (initial)

- Login → list sessions → open chat → send text → observe streamed reply
- Voice upload + transcription path
- Attachments
- Boards fetch

## Policy

- Do **not** put staging passwords in the repo.
- Prefer a dedicated CI job with secrets + opt-in (`workflow_dispatch`), not a blocker for every PR until stable.
- Product bugs found in E2E → task under `docs/tasks/pending/`.
