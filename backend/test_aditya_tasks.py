"""
Comprehensive Test Suite for Aditya's Tasks (Issues #12, #13, #14, #15, #16)
Author: Aditya Surshe (@Aditya979-coder)
"""

import os
import sys
import numpy as np

from audio_analyzer import (
    validate_audio_detailed,
    extract_acoustic_biomarkers,
    classify_voice_authenticity,
    analyze_audio_forensics,
    generate_authenticity_timeline,
    ActiveVoiceChallengeEngine,
    read_audio_data
)

SAMPLES_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "samples")


def test_issue_12_validation():
    print("\n--- Testing Issue #12: Audio Validation & Preprocessing ---")
    sr = 16000
    
    # 1. Too short
    short_samples = np.zeros(int(sr * 0.3), dtype=np.float32)
    val_short = validate_audio_detailed(short_samples, sr, 0.3)
    assert not val_short["is_valid"], "Failed to flag too short audio"
    print("  [PASS] Minimum duration check passed (< 0.6s)")

    # 2. Silent
    silent_samples = np.zeros(int(sr * 2.0), dtype=np.float32)
    val_silent = validate_audio_detailed(silent_samples, sr, 2.0)
    assert not val_silent["is_valid"], "Failed to flag silent audio"
    print("  [PASS] Silence / near-zero volume check passed")

    # 3. Severely clipped
    clipped_samples = np.ones(int(sr * 2.0), dtype=np.float32)
    val_clipped = validate_audio_detailed(clipped_samples, sr, 2.0)
    assert not val_clipped["is_valid"], "Failed to flag clipped audio"
    print("  [PASS] Excessive clipping check passed (> 18% saturation)")

    # 4. Valid sample
    with open(os.path.join(SAMPLES_DIR, "Sample_A_Genuine.wav"), "rb") as f:
        samples, rate, dur = read_audio_data(f.read())
    val_valid = validate_audio_detailed(samples, rate, dur)
    assert val_valid["is_valid"], f"Valid sample falsely rejected: {val_valid['reason']}"
    print(f"  [PASS] Valid audio verified (SNR: {val_valid['snr_db']} dB, RMS: {val_valid['rms_dbfs']} dBFS)")


def test_issue_13_biomarkers():
    print("\n--- Testing Issue #13: Feature Extraction (Pitch, Jitter, Spectral) ---")
    with open(os.path.join(SAMPLES_DIR, "Sample_A_Genuine.wav"), "rb") as f:
        samples_a, rate, _ = read_audio_data(f.read())
    with open(os.path.join(SAMPLES_DIR, "Sample_B_Suspicious_Cloned.wav"), "rb") as f:
        samples_b, rate, _ = read_audio_data(f.read())

    feat_a = extract_acoustic_biomarkers(samples_a, rate)
    feat_b = extract_acoustic_biomarkers(samples_b, rate)

    # Genuine should have higher pitch std and jitter than flat synthetic
    print(f"  Sample A (Genuine)   -> F0 std: {feat_a['f0_std']} Hz, Jitter: {feat_a['jitter_pct']}%, Flatness: {feat_a['spectral_flatness']}")
    print(f"  Sample B (Synthetic) -> F0 std: {feat_b['f0_std']} Hz, Jitter: {feat_b['jitter_pct']}%, Flatness: {feat_b['spectral_flatness']}")

    assert feat_a["f0_std"] > feat_b["f0_std"] * 5, "Genuine speech F0 std should significantly exceed flat synthetic speech F0 std"
    assert feat_a["jitter_pct"] > feat_b["jitter_pct"], "Genuine speech should exhibit higher vocal micro-tremor jitter"
    print("  [PASS] Acoustic biomarker contrast verified between genuine and synthetic speech")


def test_issue_14_classification():
    print("\n--- Testing Issue #14: Voice Authenticity Classification ---")
    
    # Test Genuine
    with open(os.path.join(SAMPLES_DIR, "Sample_A_Genuine.wav"), "rb") as f:
        res_a = analyze_audio_forensics(f.read(), "Sample_A_Genuine.wav")
    assert res_a["classification"] == "LIKELY GENUINE", f"Expected LIKELY GENUINE, got {res_a['classification']}"
    print(f"  [PASS] Sample A classified as: {res_a['classification']} (Evidence: {res_a['evidence_strength']})")

    # Test Suspicious Cloned
    with open(os.path.join(SAMPLES_DIR, "Sample_B_Suspicious_Cloned.wav"), "rb") as f:
        res_b = analyze_audio_forensics(f.read(), "Sample_B_Suspicious_Cloned.wav")
    assert res_b["classification"] == "SUSPICIOUS", f"Expected SUSPICIOUS, got {res_b['classification']}"
    print(f"  [PASS] Sample B classified as: {res_b['classification']} (Evidence: {res_b['evidence_strength']})")

    # Test Inconclusive Noisy
    with open(os.path.join(SAMPLES_DIR, "Sample_C_Inconclusive_Noisy.wav"), "rb") as f:
        res_c = analyze_audio_forensics(f.read(), "Sample_C_Inconclusive_Noisy.wav")
    assert res_c["classification"] == "INCONCLUSIVE", f"Expected INCONCLUSIVE, got {res_c['classification']}"
    print(f"  [PASS] Sample C classified as: {res_c['classification']} (Evidence: {res_c['evidence_strength']})")


def test_issue_15_timeline():
    print("\n--- Testing Issue #15: Real-Time Authenticity Timeline ---")
    with open(os.path.join(SAMPLES_DIR, "Sample_A_Genuine.wav"), "rb") as f:
        audio_bytes = f.read()

    timeline = generate_authenticity_timeline(audio_bytes, window_sec=1.5)
    assert len(timeline) >= 2, "Timeline should generate multiple window slices"
    for seg in timeline:
        assert "time" in seg and "risk" in seg and "desc" in seg, "Segment missing required fields"
        print(f"  Segment [{seg['time']}]: [{seg['risk']}] - {seg['desc'][:50]}...")
    print("  [PASS] Real-time authenticity timeline generation verified")


def test_issue_16_challenge():
    print("\n--- Testing Issue #16: Active Voice Verification Challenge ---")
    ch = ActiveVoiceChallengeEngine.generate_challenge()
    assert "challenge_code" in ch and "challenge_phrase" in ch
    print(f"  Generated challenge phrase: \"{ch['challenge_phrase']}\" (Code: {ch['challenge_code']})")

    # Test verification with valid speech
    with open(os.path.join(SAMPLES_DIR, "Sample_A_Genuine.wav"), "rb") as f:
        v_res = ActiveVoiceChallengeEngine.verify_liveness(f.read(), ch["challenge_code"])
    assert v_res["verified"] and v_res["liveness_status"] == "PASSED"
    print(f"  Liveness verification: {v_res['liveness_status']} - {v_res['reason']}")
    print("  [PASS] Active voice challenge and anti-replay liveness protocol verified")


if __name__ == "__main__":
    print("=" * 60)
    print("TESTING ADITYA'S ASSIGNED TASKS (#12, #13, #14, #15, #16)")
    print("=" * 60)
    
    test_issue_12_validation()
    test_issue_13_biomarkers()
    test_issue_14_classification()
    test_issue_15_timeline()
    test_issue_16_challenge()
    
    print("\n" + "=" * 60)
    print("ALL OF ADITYA'S ASSIGNED TASKS PASSED WITH 100% SUCCESS!")
    print("=" * 60)
