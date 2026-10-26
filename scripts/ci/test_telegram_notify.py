#!/usr/bin/env python3
"""Unit tests for telegram_notify parsers (no network)."""

from __future__ import annotations

import tempfile
import unittest
from pathlib import Path

from telegram_notify import (
    build_tests_message,
    parse_jacoco_line_percent,
    parse_junit_xml_files,
)


class TelegramNotifyParsersTest(unittest.TestCase):
    def test_parse_junit_and_jacoco(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            junit_dir = root / "app" / "build" / "test-results" / "testDebugUnitTest"
            junit_dir.mkdir(parents=True)
            (junit_dir / "TEST-Demo.xml").write_text(
                """<?xml version="1.0"?>
                <testsuite name="Demo" tests="2" failures="1" errors="0" skipped="0">
                  <testcase classname="a.A" name="ok"/>
                  <testcase classname="a.A" name="bad">
                    <failure message="x">boom</failure>
                  </testcase>
                </testsuite>
                """,
                encoding="utf-8",
            )
            jacoco = root / "app" / "build" / "reports" / "jacoco" / "jacocoTestReport"
            jacoco.mkdir(parents=True)
            (jacoco / "jacocoTestReport.xml").write_text(
                """<?xml version="1.0"?>
                <report name="app">
                  <counter type="LINE" missed="2" covered="8"/>
                </report>
                """,
                encoding="utf-8",
            )

            counts, failed = parse_junit_xml_files([junit_dir / "TEST-Demo.xml"])
            self.assertEqual(counts["passed"], 1)
            self.assertEqual(counts["failed"], 1)
            self.assertEqual(failed, ["a.A.bad"])
            self.assertEqual(parse_jacoco_line_percent([root]), 80.0)

            msg = build_tests_message("failure", ui_outcome="skipped", artifacts_dir=root)
            self.assertIn("Knowledge Base Android", msg)
            self.assertIn("a.A.bad", msg)
            self.assertIn("80.0%", msg)


if __name__ == "__main__":
    unittest.main()
