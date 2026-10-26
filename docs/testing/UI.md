# UI tests (Compose)

## Approach

Mirror iOS `KnowledgeBaseAppUITests`:

- Instrumentation argument / BuildConfig flag **`UITesting`** forces stub API/chat/boards.
- CI does **not** need staging credentials for smoke UI tests.

## Scenarios (smoke — target after shell exists)

- Root tabs: Chats / Overview / Settings
- Open seeded demo session → messages visible
- New session → type → send → user bubble + stub reply
- Overview: stub board row → Structured UI nodes
- Error / retry affordances when implemented

## Accessibility / test tags

Use stable `Modifier.testTag(...)` / semantics — e.g. `main.newSession`, `session.row.<id>`, `composer.textField`, `composer.send`.

## Run

```bash
./gradlew connectedDebugAndroidTest
# or CI-documented gradle task once wired
```

UI coverage is **not** a substitute for the 80% unit/line JaCoCo gate; both are required for user-facing changes.
