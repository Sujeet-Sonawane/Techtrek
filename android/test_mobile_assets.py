"""
Test Suite for NEXUS Mobile App Foundation & UI/UX Features
Assigned to: Anuj Shelke (@Anuj-Shelke)
Covers Issues:
 - #6: [P0] #2: Mobile App Foundation & Android APK Pipeline
 - #7: [P0] #3: Audio Input & Sample Selection System
 - #8: [P0] #10: Safety Guidance & Verification Checklist
 - #9: [P0] #11: Mobile Result Screen & Human Explainability
 - #10: [P1] #13: Live Call Demo Simulation Mode
 - #11: [P1] #15: Post-Call Trust Report Generation
"""

import os
import re

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ANDROID_DIR = os.path.join(BASE_DIR, "android")
ASSETS_DIR = os.path.join(ANDROID_DIR, "app", "src", "main", "assets")
MANIFEST_PATH = os.path.join(ANDROID_DIR, "app", "src", "main", "AndroidManifest.xml")
INDEX_PATH = os.path.join(ASSETS_DIR, "index.html")
APP_JS_PATH = os.path.join(ASSETS_DIR, "app.js")
STYLES_CSS_PATH = os.path.join(ASSETS_DIR, "styles.css")
APK_PATH = os.path.join(BASE_DIR, "NEXUS-debug.apk")

def test_issue_6_app_foundation_and_apk():
    """Issue #6 / Issue #2: Mobile App Foundation & Android APK Pipeline"""
    # 1. Android Manifest permissions
    assert os.path.exists(MANIFEST_PATH), "AndroidManifest.xml missing"
    with open(MANIFEST_PATH, "r", encoding="utf-8") as f:
        manifest_content = f.read()
    assert "android.permission.RECORD_AUDIO" in manifest_content, "RECORD_AUDIO permission missing"
    assert "android.permission.INTERNET" in manifest_content, "INTERNET permission missing"

    # 2. Stylesheet dark mode theme (#0B0F17)
    with open(STYLES_CSS_PATH, "r", encoding="utf-8") as f:
        css_content = f.read()
    assert "#0B0F17" in css_content or "#0b0f17" in css_content, "Theme #0B0F17 missing in CSS"
    assert "viewport-fit=cover" in open(INDEX_PATH, encoding="utf-8").read()

    # 3. APK deliverable present
    assert os.path.exists(APK_PATH), "NEXUS-debug.apk missing"
    assert os.path.getsize(APK_PATH) > 1000000, "NEXUS-debug.apk size invalid"
    print("[PASS] Issue #6: Mobile App Foundation & Android APK Pipeline verified")

def test_issue_7_audio_input_and_samples():
    """Issue #7 / Issue #3: Audio Input & Sample Selection System"""
    with open(INDEX_PATH, "r", encoding="utf-8") as f:
        html = f.read()
    with open(APP_JS_PATH, "r", encoding="utf-8") as f:
        js = f.read()

    # Audio input tabs
    assert 'tab-btn-samples' in html
    assert 'tab-btn-upload' in html
    assert 'tab-btn-record' in html
    assert 'file-uploader' in html
    assert 'samples-list-container' in html
    assert 'btn-analyze-voice' in html

    # Benchmark samples loaded in JS
    assert 'sample_a' in js
    assert 'sample_b' in js
    assert 'sample_c' in js
    assert 'sample_d' in js
    print("[PASS] Issue #7: Audio Input & Sample Selection System verified")

def test_issue_8_safety_guidance_and_checklist():
    """Issue #8 / Issue #10: Safety Guidance & Verification Checklist"""
    with open(INDEX_PATH, "r", encoding="utf-8") as f:
        html = f.read()
    with open(APP_JS_PATH, "r", encoding="utf-8") as f:
        js = f.read()

    # Verification checklist modal
    assert 'checklist-modal' in html
    assert 'openChecklistModal' in js
    assert 'closeChecklistModal' in js
    assert 'Stop and pause' in html
    assert 'Call back on a trusted number' in html
    assert 'Never share credentials' in html
    assert 'action-banner-urgent' in html
    print("[PASS] Issue #8: Safety Guidance & Verification Checklist verified")

def test_issue_9_mobile_result_screen():
    """Issue #9 / Issue #11: Mobile Result Screen & Human Explainability"""
    with open(INDEX_PATH, "r", encoding="utf-8") as f:
        html = f.read()
    with open(APP_JS_PATH, "r", encoding="utf-8") as f:
        js = f.read()

    assert 'screen-result' in html
    assert 'result-badge-card' in html
    assert 'result-status-text' in html
    assert 'result-risk-chip' in html
    assert 'result-evidence-val' in html
    assert 'result-quality-val' in html
    assert 'result-signals-list' in html
    assert 'result-explanation-text' in html
    assert 'tech-details-box' in html
    assert 'displayResults' in js
    print("[PASS] Issue #9: Mobile Result Screen & Human Explainability verified")

def test_issue_10_live_call_simulation():
    """Issue #10 / Issue #13: Live Call Demo Simulation Mode"""
    with open(INDEX_PATH, "r", encoding="utf-8") as f:
        html = f.read()
    with open(APP_JS_PATH, "r", encoding="utf-8") as f:
        js = f.read()

    assert 'screen-live-call' in html
    assert 'waveform-canvas' in html
    assert 'live-call-timer' in html
    assert 'live-timeline-strip' in html
    assert 'live-voice-alert' in html
    assert 'live-financial-alert' in html
    assert 'startLiveCall' in js
    assert 'endLiveCall' in js
    assert 'startWaveformAnimation' in js
    print("[PASS] Issue #10: Live Call Demo Simulation Mode verified")

def test_issue_11_post_call_trust_report():
    """Issue #11 / Issue #15: Post-Call Trust Report Generation"""
    with open(INDEX_PATH, "r", encoding="utf-8") as f:
        html = f.read()
    with open(APP_JS_PATH, "r", encoding="utf-8") as f:
        js = f.read()

    assert 'screen-report' in html
    assert 'report-metrics-grid' in html
    assert 'Why was this flagged?' in html
    assert 'RECOMMENDED ACTION' in html
    assert 'navigateTo(\'report\')' in js or 'navigateTo("report")' in js
    print("[PASS] Issue #11: Post-Call Trust Report Generation verified")

if __name__ == "__main__":
    print("=" * 60)
    print("RUNNING ANUJ SHELKE MOBILE DELIVERABLES TEST SUITE")
    print("=" * 60)
    test_issue_6_app_foundation_and_apk()
    test_issue_7_audio_input_and_samples()
    test_issue_8_safety_guidance_and_checklist()
    test_issue_9_mobile_result_screen()
    test_issue_10_live_call_simulation()
    test_issue_11_post_call_trust_report()
    print("=" * 60)
    print("ALL ANUJ SHELKE MOBILE TASKS PASSED WITH 100% SUCCESS!")
    print("=" * 60)
