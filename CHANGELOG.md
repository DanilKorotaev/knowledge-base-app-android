# Changelog

All notable changes to the Android Knowledge Base app are documented here.

Format inspired by [Keep a Changelog](https://keepachangelog.com/). Versioning: [SemVer](https://semver.org/).

## [Unreleased]

### Fixed

- CI: upgrade `android-actions/setup-android` to v4 and install `platform-tools` only (Google removed the obsolete `tools` package).

### Added

- Gradle multi-module skeleton (`:app`, `:core:model`, `:core:network`), Hilt, Compose placeholder + stub API.
- Unit tests, Compose UI test, JaCoCo coverage gate ≥ 80%, GitHub Actions CI (check + emulator UI).
- Documentation tree, technology stack lock, agent rules (TDD, coverage, Gradle CI parity).
- Initial pending tasks for SemVer, Play/RuStore, and product MVP.
