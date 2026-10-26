#!/usr/bin/env python3
"""
Send Knowledge Base Android CI/CD notifications to Telegram.

Requires secrets (GitHub Actions or env):
  TELEGRAM_BOT_TOKEN — bot token from @BotFather
  TELEGRAM_CHAT_ID   — chat / channel id

Optional:
  TELEGRAM_NOTIFY_DISABLED=1 — skip sending (no error)
  MIN_COVERAGE — coverage gate percent (default 80)
"""

from __future__ import annotations

import argparse
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[2]
MAX_FAILED_TESTS = 25


def html_escape(text: str) -> str:
    return (
        text.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
    )


def github_run_url() -> str | None:
    server = os.environ.get("GITHUB_SERVER_URL", "https://github.com").rstrip("/")
    repo = os.environ.get("GITHUB_REPOSITORY", "").strip()
    run_id = os.environ.get("GITHUB_RUN_ID", "").strip()
    if repo and run_id:
        return f"{server}/{repo}/actions/runs/{run_id}"
    return None


def ci_context_lines() -> list[str]:
    lines: list[str] = []
    ref = os.environ.get("GITHUB_REF_NAME", "").strip()
    event = os.environ.get("GITHUB_EVENT_NAME", "").strip()
    if ref:
        lines.append(f"Ветка: <code>{html_escape(ref)}</code>")
    if event:
        lines.append(f"Событие: <code>{html_escape(event)}</code>")
    url = github_run_url()
    if url:
        lines.append(f'<a href="{html_escape(url)}">Workflow run</a>')
    return lines


def send_telegram(text: str) -> None:
    if os.environ.get("TELEGRAM_NOTIFY_DISABLED", "").strip() in ("1", "true", "yes"):
        print("TELEGRAM_NOTIFY_DISABLED: skip")
        return

    token = os.environ.get("TELEGRAM_BOT_TOKEN", "").strip()
    chat_id = os.environ.get("TELEGRAM_CHAT_ID", "").strip()
    if not token or not chat_id:
        print("TELEGRAM_BOT_TOKEN or TELEGRAM_CHAT_ID not set; skip notify")
        return

    url = f"https://api.telegram.org/bot{token}/sendMessage"
    payload = urllib.parse.urlencode(
        {
            "chat_id": chat_id,
            "text": text,
            "parse_mode": "HTML",
            "disable_web_page_preview": "true",
        }
    ).encode("utf-8")
    request = urllib.request.Request(url, data=payload, method="POST")
    try:
        with urllib.request.urlopen(request, timeout=45) as response:
            body = json.loads(response.read().decode("utf-8"))
    except urllib.error.HTTPError as exc:
        err_body = exc.read().decode("utf-8", errors="replace")
        print(f"Telegram API HTTP {exc.code}: {err_body}", file=sys.stderr)
        sys.exit(1)

    if not body.get("ok"):
        print(f"Telegram API error: {body}", file=sys.stderr)
        sys.exit(1)


def empty_counts() -> dict[str, int]:
    return {"passed": 0, "failed": 0, "skipped": 0, "total": 0}


def _path_kind(path: Path) -> str | None:
    """Classify a JUnit XML path as unit, ui, or ignore."""
    parts = [p.lower() for p in path.parts]
    joined = "/".join(parts)
    if "jacoco" in parts or path.name == "jacocoTestReport.xml":
        return None
    if "androidtest" in joined or "connected" in parts:
        return "ui"
    if "testdebugunittest" in joined or "test-results" in joined or path.name.startswith("TEST-"):
        return "unit"
    return None


def iter_junit_xml(roots: list[Path]) -> list[Path]:
    files: list[Path] = []
    seen: set[Path] = set()
    for root in roots:
        if not root.exists():
            continue
        for path in root.rglob("*.xml"):
            resolved = path.resolve()
            if resolved in seen:
                continue
            if _path_kind(path) is None:
                continue
            seen.add(resolved)
            files.append(path)
    return files


def parse_junit_xml_files(paths: list[Path]) -> tuple[dict[str, int], list[str]]:
    counts = empty_counts()
    failed_names: list[str] = []

    for path in paths:
        try:
            tree = ET.parse(path)
        except (ET.ParseError, OSError):
            continue
        root_el = tree.getroot()
        if root_el.tag == "testsuite":
            suites = [root_el]
        elif root_el.tag == "testsuites":
            suites = list(root_el.findall("testsuite"))
        else:
            suites = list(root_el.findall("testsuite"))

        for suite in suites:
            if suite.tag != "testsuite":
                continue
            tests = int(suite.attrib.get("tests", 0) or 0)
            failures = int(suite.attrib.get("failures", 0) or 0)
            errors = int(suite.attrib.get("errors", 0) or 0)
            skipped = int(suite.attrib.get("skipped", 0) or 0)
            failed = failures + errors
            counts["total"] += tests
            counts["failed"] += failed
            counts["skipped"] += skipped
            counts["passed"] += max(0, tests - failed - skipped)

            for case in suite.findall("testcase"):
                if case.find("failure") is not None or case.find("error") is not None:
                    classname = case.attrib.get("classname", "")
                    name = case.attrib.get("name", "")
                    label = f"{classname}.{name}" if classname else name
                    if label:
                        failed_names.append(label)

    deduped: list[str] = []
    seen_names: set[str] = set()
    for name in failed_names:
        if name not in seen_names:
            seen_names.add(name)
            deduped.append(name)
    return counts, deduped


def parse_jacoco_line_percent(roots: list[Path]) -> float | None:
    covered = 0
    missed = 0
    for root in roots:
        if not root.exists():
            continue
        for path in root.rglob("jacocoTestReport.xml"):
            try:
                tree = ET.parse(path)
            except (ET.ParseError, OSError):
                continue
            for counter in tree.getroot().findall("counter"):
                if counter.attrib.get("type") == "LINE":
                    covered += int(counter.attrib.get("covered", 0) or 0)
                    missed += int(counter.attrib.get("missed", 0) or 0)
    total = covered + missed
    if total == 0:
        return None
    return round(100.0 * covered / total, 2)


def format_suite_block(
    title: str,
    outcome: str,
    counts: dict[str, int],
    failed_names: list[str],
    *,
    coverage: float | None = None,
    min_cov: str | None = None,
    ran: bool,
) -> list[str]:
    lines = [f"<b>{html_escape(title)}</b>"]

    if not ran or outcome == "skipped":
        lines.append("Статус: <code>skipped</code>")
        return lines

    suite_ok = outcome == "success" and counts["failed"] == 0
    status = "✅ success" if suite_ok else "❌ failure"
    if outcome == "cancelled":
        status = "⏹ cancelled"
    lines.extend(
        [
            f"Статус: <code>{html_escape(status)}</code>",
            f"Успешно: <b>{counts['passed']}</b>",
            f"Провалено: <b>{counts['failed']}</b>",
            f"Пропущено: <b>{counts['skipped']}</b>",
        ]
    )

    if coverage is not None and min_cov is not None:
        lines.append(f"Покрытие (JaCoCo LINE): <b>{coverage}%</b> (мин. {html_escape(min_cov)}%)")
    elif title.startswith("Unit") and ran and outcome == "success" and coverage is None:
        lines.append("Покрытие: отчёт JaCoCo не найден")

    if failed_names:
        lines.append("Упавшие:")
        for name in failed_names[:MAX_FAILED_TESTS]:
            lines.append(f"• <code>{html_escape(name)}</code>")
        if len(failed_names) > MAX_FAILED_TESTS:
            lines.append(f"• … и ещё {len(failed_names) - MAX_FAILED_TESTS}")

    if outcome != "success" and counts["failed"] == 0 and ran:
        lines.append(
            "<i>Job завершился с ошибкой (сборка / покрытие / Gradle), "
            "но Failed test case в JUnit XML нет.</i>"
        )

    return lines


def default_report_roots(artifacts_dir: Path | None) -> list[Path]:
    # When CI downloads artifacts into a dedicated dir, only scan that tree
    # (repo checkout has no Gradle reports from other jobs).
    if artifacts_dir is not None:
        return [artifacts_dir] if artifacts_dir.exists() else []
    roots = [REPO_ROOT]
    if (REPO_ROOT / "ci-artifacts").exists():
        roots.append(REPO_ROOT / "ci-artifacts")
    return roots


def build_tests_message(
    outcome: str,
    *,
    ui_outcome: str = "skipped",
    artifacts_dir: Path | None = None,
) -> str:
    roots = default_report_roots(artifacts_dir)
    all_junit = iter_junit_xml(roots)
    unit_paths = [p for p in all_junit if _path_kind(p) == "unit"]
    ui_paths = [p for p in all_junit if _path_kind(p) == "ui"]

    unit_counts, unit_failed = parse_junit_xml_files(unit_paths)
    ui_counts, ui_failed = (
        parse_junit_xml_files(ui_paths) if ui_outcome not in {"skipped", ""} else (empty_counts(), [])
    )
    coverage = parse_jacoco_line_percent(roots)
    min_cov = os.environ.get("MIN_COVERAGE", "80").strip()

    unit_ran = outcome != "skipped"
    ui_ran = ui_outcome not in {"skipped", ""}
    unit_ok = outcome == "success" and unit_counts["failed"] == 0
    ui_ok = (not ui_ran) or (ui_outcome == "success" and ui_counts["failed"] == 0)
    success = unit_ok and ui_ok

    title = (
        "✅ <b>Knowledge Base Android — тесты пройдены</b>"
        if success
        else "❌ <b>Knowledge Base Android — тесты упали</b>"
    )

    lines = [title, ""] + ci_context_lines() + [""]
    lines.extend(
        format_suite_block(
            "Unit + coverage",
            outcome,
            unit_counts,
            unit_failed,
            coverage=coverage,
            min_cov=min_cov,
            ran=unit_ran,
        )
    )
    lines.append("")
    lines.extend(
        format_suite_block(
            "UI (instrumented)",
            ui_outcome,
            ui_counts,
            ui_failed,
            ran=ui_ran,
        )
    )
    return "\n".join(lines)


def build_play_message(outcome: str) -> str:
    version = "?"
    version_path = REPO_ROOT / "VERSION"
    if version_path.is_file():
        version = version_path.read_text(encoding="utf-8").strip() or "?"

    meta_path = REPO_ROOT / "build" / "ci" / "ci_play.json"
    meta: dict = {}
    if meta_path.is_file():
        try:
            meta = json.loads(meta_path.read_text(encoding="utf-8"))
        except (json.JSONDecodeError, OSError):
            meta = {}
    version = str(meta.get("version", version))
    version_code = str(meta.get("version_code", "?"))
    track = str(meta.get("track", "internal"))

    ok = outcome == "success"
    title = (
        "✅ <b>Knowledge Base Android — Play</b>"
        if ok
        else "❌ <b>Knowledge Base Android — загрузка в Play не удалась</b>"
    )
    lines = [
        title,
        "",
        *ci_context_lines(),
        "",
        f"Версия: <b>{html_escape(version)}</b> (versionCode {html_escape(version_code)})",
        f"Трек: <code>{html_escape(track)}</code>",
    ]
    repo = os.environ.get("GITHUB_REPOSITORY", "").strip()
    server = os.environ.get("GITHUB_SERVER_URL", "https://github.com").rstrip("/")
    if repo:
        changelog_url = f"{server}/{repo}/blob/main/CHANGELOG.md"
        lines.append(f'<a href="{html_escape(changelog_url)}">CHANGELOG</a>')
        if version not in ("", "?"):
            tag = f"android/v{version}"
            tag_url = f"{server}/{repo}/releases/tag/{tag}"
            lines.append(f'Tag: <a href="{html_escape(tag_url)}"><code>{html_escape(tag)}</code></a>')

    lines.append("")
    if ok:
        lines.append("AAB отправлен в Google Play (см. трек выше).")
    else:
        lines.append("См. логи workflow (signing / supply / Play API).")
    return "\n".join(lines)


def main() -> None:
    parser = argparse.ArgumentParser(description="Telegram CI notifications for Knowledge Base Android")
    parser.add_argument("event", choices=["tests", "play"], help="Notification type")
    parser.add_argument(
        "--outcome",
        required=True,
        choices=["success", "failure", "cancelled", "skipped"],
        help="Primary job outcome (unit+coverage for event=tests)",
    )
    parser.add_argument(
        "--ui-outcome",
        default="skipped",
        choices=["success", "failure", "cancelled", "skipped"],
        help="UI instrumented tests job outcome (event=tests)",
    )
    parser.add_argument(
        "--artifacts-dir",
        type=Path,
        default=None,
        help="Directory with downloaded CI artifacts",
    )
    args = parser.parse_args()

    if args.event == "tests":
        message = build_tests_message(
            args.outcome,
            ui_outcome=args.ui_outcome,
            artifacts_dir=args.artifacts_dir,
        )
    else:
        message = build_play_message(args.outcome)

    print("--- preview ---")
    print(message)
    print("--- end ---")
    send_telegram(message)
    print("Telegram notification sent (or skipped).")


if __name__ == "__main__":
    main()
