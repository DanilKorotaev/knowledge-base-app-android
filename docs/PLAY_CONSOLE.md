# Google Play

Primary store for the Android KB App.

## Prerequisites (owner)

1. Google Play Console developer account.
2. Create application: **Knowledge Base** (or final public name).
3. Package: `com.coredan.knowledgebaseapp` (must match Gradle `applicationId`).
4. App signing by Google Play enabled; keep **upload** keystore backup offline.
5. Service account with Play Android Developer API access → JSON key → GitHub secret.

## Tracks (recommended order)

1. **Internal testing** — CI auto-upload after green `main`
2. **Closed testing** — wider device matrix
3. **Open / Production** — manual promote until process is trusted

## CI upload (planned)

- Workflow: `.github/workflows/deploy-play.yml`
- Tooling: Fastlane `supply` **or** Gradle Play Publisher (choose in `task-ops-google-play-internal.md`)
- Input: signed **AAB**
- Gate: successful `ci.yml` on the same commit + coverage ≥ 80%

## Checklist before first upload

- [ ] Store listing draft (EN + RU short/full description)
- [ ] Screenshots for phone (and tablet if claimed)
- [ ] Privacy policy URL
- [ ] Data safety form
- [ ] Content rating questionnaire
- [ ] Upload keystore + CI secrets configured
- [ ] `versionCode` / `versionName` wired from `VERSION` + CI

## Do not

- Commit keystores or service account JSON
- Upload to production from the first CI green without an explicit promote step (until owner says otherwise)
