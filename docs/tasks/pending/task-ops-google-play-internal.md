# Task: Google Play internal testing upload

**Status:** pending  
**Depends on:** skeleton, SemVer, signing secrets

## Goal

CI can upload a signed AAB to Play **internal testing** after green CI.

## Deliverables

- Choose tooling: Fastlane `supply` **or** Gradle Play Publisher — document in `PLAY_CONSOLE.md`
- `deploy-play.yml` gated on green `ci.yml`
- GitHub secrets checklist completed with owner
- First internal track build recorded in `RELEASES.md`

## Acceptance

- [ ] Manual `workflow_dispatch` uploads successfully
- [ ] Auto path after `main` CI documented (on/off per owner preference)
- [ ] Keystore never in git
