"""
Comprehensive Integration & Verification Test Suite for NEXUS Backend
Assigned to: Sujeet Sonawane (@Sujeet-Sonawane)
Covers Issues:
 - #1: Repository Architecture Baseline & Workspace Setup
 - #2: FastAPI /analyze Endpoint & API Contract
 - #3: Risk Assessment Engine
 - #4: Financial Context & Sensitive Keyword Detection
 - #5: End-to-End Integration & Resilient Offline Fallback
"""

import os
import sys
from fastapi.testclient import TestClient

# Ensure backend directory is in path
sys.path.insert(0, os.path.dirname(__file__))

from main import app
from financial_detector import detect_financial_context
from risk_engine import evaluate_risk

client = TestClient(app)

def test_health_endpoint():
    """Issue #2: /health endpoint verification"""
    response = client.get("/health")
    assert response.status_code == 200, f"Health check failed: {response.text}"
    data = response.json()
    assert data.get("status") == "online"
    assert "NEXUS" in data.get("service", "")
    print("[PASS] Issue #2: /health endpoint verified")

def test_samples_catalog():
    """Issue #1 & #2: Demo samples catalog endpoint"""
    response = client.get("/samples")
    assert response.status_code == 200
    samples = response.json()
    assert len(samples) >= 4, "Expected at least 4 demo samples"
    sample_ids = [s["id"] for s in samples]
    assert "sample_a" in sample_ids
    assert "sample_b" in sample_ids
    assert "sample_c" in sample_ids
    assert "sample_d" in sample_ids
    print("[PASS] Issue #1: Benchmark audio samples verified in catalog")

def test_financial_keyword_detection():
    """Issue #4: Financial context and sensitive keyword detection"""
    # Test financial + urgency
    res1 = detect_financial_context("Transfer ₹50,000 immediately to the UPI ID right now!")
    assert res1["financial_context"] is True
    assert res1["urgency_detected"] is True
    assert any("UPI" in t or "Transfer" in t or "Currency" in t for t in res1["financial_triggers"])

    # Test sensitive credentials (OTP, PIN)
    res2 = detect_financial_context("Please share your OTP and UPI PIN to avoid account suspension.")
    assert res2["financial_context"] is True
    assert any("OTP" in t for t in res2["financial_triggers"])
    assert any("PIN" in t for t in res2["financial_triggers"])

    # Test clean conversation
    res3 = detect_financial_context("Good morning team, let's review the design mockups.")
    assert res3["financial_context"] is False
    assert res3["urgency_detected"] is False
    assert len(res3["financial_triggers"]) == 0
    print("[PASS] Issue #4: Financial context & sensitive keyword detection verified")

def test_risk_assessment_engine():
    """Issue #3: Holistic Risk Assessment Matrix"""
    # 1. Suspicious voice + any context -> HIGH
    risk1 = evaluate_risk("SUSPICIOUS", "High", False, False, ["Mechanical constancy"])
    assert risk1["risk_level"] == "HIGH"

    # 2. Suspicious voice + financial request -> HIGH
    risk2 = evaluate_risk("SUSPICIOUS", "High", True, True, ["Acoustic anomalies"])
    assert risk2["risk_level"] == "HIGH"

    # 3. Inconclusive voice + non-financial -> MEDIUM
    risk3 = evaluate_risk("INCONCLUSIVE", "Low", False, False, ["High noise floor"])
    assert risk3["risk_level"] == "MEDIUM"

    # 4. Inconclusive voice + financial -> HIGH
    risk4 = evaluate_risk("INCONCLUSIVE", "Low", True, False, ["High noise floor"])
    assert risk4["risk_level"] == "HIGH"

    # 5. Genuine voice + non-financial -> LOW
    risk5 = evaluate_risk("LIKELY GENUINE", "Strong", False, False, ["Natural pitch"])
    assert risk5["risk_level"] == "LOW"

    # 6. Genuine voice + financial + urgent -> HIGH
    risk6 = evaluate_risk("LIKELY GENUINE", "Strong", True, True, ["Natural pitch"])
    assert risk6["risk_level"] == "HIGH"

    print("[PASS] Issue #3: Risk assessment engine matrix verified")

def test_analyze_endpoint_contract():
    """Issue #2: /analyze API contract and schema verification"""
    # Test with sample_a (Genuine)
    res_a = client.post("/analyze", data={"sample_id": "sample_a"})
    assert res_a.status_code == 200
    data_a = res_a.json()
    assert data_a["classification"] == "LIKELY GENUINE"
    assert data_a["risk_level"] == "LOW"
    assert "verification_steps" in data_a
    assert "signals" in data_a
    assert "explanation" in data_a

    # Test with sample_b (Suspicious Cloned)
    res_b = client.post("/analyze", data={"sample_id": "sample_b"})
    assert res_b.status_code == 200
    data_b = res_b.json()
    assert data_b["classification"] == "SUSPICIOUS"
    assert data_b["risk_level"] == "HIGH"

    # Test with sample_d (Urgent Transfer)
    res_d = client.post("/analyze", data={"sample_id": "sample_d"})
    assert res_d.status_code == 200
    data_d = res_d.json()
    assert data_d["financial_context"] is True
    assert data_d["risk_level"] == "HIGH"

    print("[PASS] Issue #2: /analyze endpoint conforms to NEXUS contract")

def test_offline_fallback_compatibility():
    """Issue #5: End-to-End Integration & Resilient Offline Fallback"""
    # Verify that the response schema provided by FastAPI exactly mirrors
    # the client-side JavaScript audio engine fields in app.js
    res = client.post("/analyze", data={"sample_id": "sample_a"})
    data = res.json()
    expected_fields = [
        "valid", "classification", "risk_level", "evidence_strength",
        "audio_quality", "signals", "financial_context", "explanation",
        "recommendation", "verification_steps"
    ]
    for field in expected_fields:
        assert field in data, f"Missing contract field: {field}"
    print("[PASS] Issue #5: Response contract parity verified for offline fallback")

if __name__ == "__main__":
    print("=" * 60)
    print("RUNNING NEXUS FULL API & RISK ENGINE TEST SUITE")
    print("=" * 60)
    test_health_endpoint()
    test_samples_catalog()
    test_financial_keyword_detection()
    test_risk_assessment_engine()
    test_analyze_endpoint_contract()
    test_offline_fallback_compatibility()
    print("=" * 60)
    print("ALL API AND RISK ENGINE TESTS PASSED SUCCESSFULLY!")
    print("=" * 60)
