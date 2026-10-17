"""
Backend test runner to verify all samples and classification criteria.
"""

import sys
import os
from audio_analyzer import analyze_audio_forensics
from financial_detector import detect_financial_context
from risk_engine import evaluate_risk

SAMPLES_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "samples")

tests = [
    {
        "name": "Sample A (Genuine)",
        "file": "Sample_A_Genuine.wav",
        "text": "Hello, this is Rahul. Just following up on the quarterly roadmap review.",
        "expected_class": "LIKELY GENUINE",
        "expected_risk": "LOW"
    },
    {
        "name": "Sample B (Suspicious Cloned)",
        "file": "Sample_B_Suspicious_Cloned.wav",
        "text": "Hello, this is Rahul. Please process the pending system invoice today.",
        "expected_class": "SUSPICIOUS",
        "expected_risk": "HIGH"
    },
    {
        "name": "Sample C (Inconclusive Noisy)",
        "file": "Sample_C_Inconclusive_Noisy.wav",
        "text": "Hello... can you hear me...",
        "expected_class": "INCONCLUSIVE",
        "expected_risk": "MEDIUM"
    },
    {
        "name": "Sample D (Financial Request)",
        "file": "Sample_D_Financial_Urgent_Transfer.wav",
        "text": "Transfer ₹50,000 immediately to the UPI ID right now!",
        "expected_risk": "HIGH",
        "expected_financial": True
    }
]

print("=" * 60)
print("NEXUS BACKEND VERIFICATION TEST")
print("=" * 60)

all_passed = True

for t in tests:
    file_path = os.path.join(SAMPLES_DIR, t["file"])
    with open(file_path, "rb") as f:
        audio_bytes = f.read()
        
    forensic = analyze_audio_forensics(audio_bytes, t["file"])
    financial = detect_financial_context(t["text"])
    risk = evaluate_risk(
        forensic["classification"],
        forensic["evidence_strength"],
        financial["financial_context"],
        financial["urgency_detected"],
        forensic["signals"]
    )
    
    print(f"\n[TEST] {t['name']}")
    print(f"  Classification : {forensic['classification']} (Expected: {t.get('expected_class', 'ANY')})")
    print(f"  Risk Level     : {risk['risk_level']} (Expected: {t.get('expected_risk', 'ANY')})")
    print(f"  Financial Ctx  : {financial['financial_context']}")
    if financial['financial_triggers']:
        print(f"  Triggers       : {financial['financial_triggers']}")
    print(f"  Signals        : {forensic['signals'][:2]}")
    print(f"  Recommendation : {risk['recommendation'][:60]}...")
    
    passed = True
    if "expected_class" in t and forensic["classification"] != t["expected_class"]:
        passed = False
    if "expected_risk" in t and risk["risk_level"] != t["expected_risk"]:
        passed = False
    if "expected_financial" in t and financial["financial_context"] != t["expected_financial"]:
        passed = False
        
    if not passed:
        print(f"  [FAIL] MISMATCH in test {t['name']}")
        all_passed = False
    else:
        print(f"  [PASS] PASSED")

print("\n" + "=" * 60)
if all_passed:
    print("ALL 4 BENCHMARK TESTS PASSED WITH 100% ACCURACY!")
else:
    print("SOME TESTS FAILED")
print("=" * 60)
