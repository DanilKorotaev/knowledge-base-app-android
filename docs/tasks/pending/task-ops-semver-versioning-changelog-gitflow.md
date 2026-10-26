# Task: SemVer + changelog + git-flow

**Status:** pending  
**Depends on:** skeleton + CI

## Goal

Mirror iOS release discipline for Android: SemVer in `VERSION`, changelog from commits, tags `android/vX.Y.Z`, trunk-based `main`.

## Deliverables

- Scripts or Gradle tasks to sync `versionName` from `VERSION` and `versionCode` from CI
- `CHANGELOG.md` + `docs/RELEASES.md` update path
- Document bump rules already sketched in `docs/RELEASE_PROCESS.md`
- Optional: auto PATCH on successful Play deploy (like iOS TestFlight)

## Acceptance

- [ ] Documented agent workflow: commit → CI → tag
- [ ] Tag naming `android/vX.Y.Z` enforced in docs/scripts
- [ ] No force-push to `main`
