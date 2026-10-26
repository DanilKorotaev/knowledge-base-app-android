# Task: Auth — login / register

**Status:** pending  
**Depends on:** skeleton, contract sync

## Goal

Onboarding: API base URL → login/register → JWT in encrypted storage. Parity with iOS auth MVP.

## TDD

- Unit: token store, auth repository success/failure mapping
- UI: login form validation + stub success navigates to sessions

## Acceptance

- [ ] No token in plaintext SharedPreferences
- [ ] Stub mode without URL still usable for UI tests
- [ ] Coverage gate green
