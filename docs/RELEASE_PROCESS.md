# Android release process (SemVer)

Default flow — trunk-based (same idea as iOS):

```text
commit on main (good message)
  → CI (tests + coverage ≥ 80%)
  → Deploy Play (internal / closed testing first)
      → auto PATCH bump (+ CHANGELOG from commits)
      → sign AAB / upload
      → commit VERSION+CHANGELOG+RELEASES [skip ci]
      → tag android/vX.Y.Z
```

Optional: manual `workflow_dispatch` for RuStore or Play production promote.

## Version numbers

| Field | Source | Meaning |
|-------|--------|---------|
| Marketing / SemVer | Root `VERSION` | `MAJOR.MINOR.PATCH` |
| `versionName` | Same as `VERSION` | Shown to users |
| `versionCode` | Monotonic int (CI `GITHUB_RUN_NUMBER` or dedicated counter) | Play/RuStore requirement; **not** the same as PATCH |

### When to bump

| Change | Bump | How |
|--------|------|-----|
| Ordinary fix / small feature | **PATCH** | Automatic on successful store deploy |
| Notable feature | **MINOR** | Set `VERSION` to `X.Y.0`, or trailer `release-bump: minor`, or dispatch input |
| Breaking / “1.0 store-ready” | **MAJOR** | Same with `major` |

## Commit message tips

Prefer Conventional Commits: `feat:`, `fix:`, `docs:`, `test:`.

```text
feat: chat SSE streaming

release-bump: minor
```

## Agent / local workflow

1. TDD slice → `./gradlew check` green.
2. Commit with a descriptive subject (changelog line).
3. Push to `main` (when remote exists).
4. Wait for CI; store deploy only if wired and green.

## Traceability

Given `0.2.0 (versionCode 42)`:

1. `CHANGELOG.md` section `0.2.0`
2. Git tag `android/v0.2.0`
3. [RELEASES.md](RELEASES.md) row mapping versionCode ↔ store track
