# NEXUS — AI-Powered Voice Authenticity & Financial Safety
> **TechTrek 2026 Hackathon Final Deliverable**  
> *Theme:* AI-Powered Fraud Prevention & Financial Security  
> *Tagline:* **Don't Trust the Voice. Verify the Request.**

---

## 📱 Executive Overview & Deliverable

**NEXUS** is an AI-powered voice security and financial safety layer for mobile devices. As voice cloning and synthetic deepfake speech proliferate, fraudsters impersonate managers, family members, bank officials, and executives to coerce victims into high-urgency financial transfers. 

NEXUS answers the critical user question:  
👉 **"Can I safely trust this voice and this request?"**

### The Core Paradigm
> *"A genuine-sounding voice is not necessarily a trustworthy request."*

NEXUS combines:
1. **Acoustic Voice Authenticity**: Forensic bio-marker analysis (fundamental pitch trajectory `F0`, micro-jitter, spectral centroid, Wiener flatness).
2. **Conversation Context Awareness**: Sensitive Indian & global financial keyword scanning (₹ amounts, UPI, NEFT, OTP, PIN, urgency triggers).
3. **Deterministic Safety Rules**: Holistic risk computation (`LOW`, `MEDIUM`, `HIGH`) without fabricated pseudo-probabilities.
4. **Actionable Safety Guidance**: Clear, non-technical verification checklist (`VERIFY BEFORE YOU ACT`).
5. **Interactive Live Call Mode (WOW Feature)**: Real-time call monitoring with animated waveforms, live risk triggers, and an explainable authenticity timeline.

---

## 📦 Android APK Deliverable

The Android APK is compiled and ready for installation:
- **Direct APK Path:** [`NEXUS-debug.apk`](./NEXUS-debug.apk) (11.36 MB)
- **Gradle Module Output:** `android/app/build/outputs/apk/debug/app-debug.apk`

### 🚀 How to Install on Android Device
1. Transfer `NEXUS-debug.apk` to your Android phone via USB cable, Google Drive, WhatsApp, or local network.
2. Tap the APK file on your device and select **Install** (Enable *"Install unknown apps"* if prompted).
3. Open **NEXUS** from your app drawer.
4. **Offline Resilience:** The app contains embedded benchmark samples and a client-side Web Audio forensics engine, guaranteeing **100% demo reliability even in airplane mode or without Wi-Fi**.

---

## 🏛️ Architecture & System Design

```
                          ┌───────────────────────────┐
                          │   Mobile Client (Android) │
                          │     Jetpack Compose +     │
                          │   WebView Forensic Shell  │
                          └─────────────┬─────────────┘
                                        │
                    ┌───────────────────┴───────────────────┐
                    │ (Online / Wi-Fi)                      │ (Offline / No Wi-Fi)
                    ▼                                       ▼
       ┌────────────────────────┐              ┌────────────────────────┐
       │ FastAPI Backend Server │              │ Embedded Client-Side   │
       │   `POST /analyze`      │              │ Web Audio Engine       │
       └────────────┬───────────┘              └────────────┬───────────┘
                    │                                       │
      ┌─────────────┴─────────────┐           ┌─────────────┴─────────────┐
      ▼                           ▼           ▼                           ▼
┌──────────────┐           ┌─────────────┐ ┌──────────────┐           ┌─────────────┐
│ Acoustic Bio-│           │  Financial  │ │ Autocorr F0  │           │ Financial   │
│ Marker Engine│           │ Context NLP │ │ & Spectral   │           │ Regex &     │
│ (F0, Jitter) │           │ (UPI, ₹, OTP│ │ Moments      │           │ Urgency NLP │
└──────┬───────┘           └──────┬──────┘ └──────┬───────┘           └──────┬──────┘
       │                          │               │                          │
       └────────────┬─────────────┘               └────────────┬─────────────┘
                    ▼                                          ▼
       ┌────────────────────────┐                 ┌────────────────────────┐
       │ Holistic Risk Engine   │                 │ Holistic Risk Engine   │
       │ (Defined Safety Rules) │                 │ (Defined Safety Rules) │
       └────────────┬───────────┘                 └────────────┬───────────┘
                    ▼                                          ▼
       ┌────────────────────────────────────────────────────────┐
       │               Final Decision & Safety Output           │
       │  • Voice: LIKELY GENUINE / SUSPICIOUS / INCONCLUSIVE   │
       │  • Risk: LOW / MEDIUM / HIGH                           │
       │  • Financial Context: True / False                     │
       │  • Action: VERIFY BEFORE YOU ACT (Checklist)           │
       └────────────────────────────────────────────────────────┘
```

---

## 🧪 Benchmark Test Samples

NEXUS includes 4 pre-generated test recordings in [`samples/`](./samples/):

| Sample | Input Voice Type | Transcript / Context | Forensic Signals | Expected Result | Risk Level |
|---|---|---|---|---|---|
| **Sample A** | Natural Human Voice | Roadmap discussion, natural cadence | Natural pitch variation (`F0 std > 25 Hz`), organic harmonics | 🟢 **LIKELY GENUINE** | **LOW** |
| **Sample B** | AI Cloned Speech | Flat robotic cadence, system invoice | Constrained pitch (`F0 std < 12 Hz`), vocoder flatness | 🔴 **SUSPICIOUS** | **HIGH** |
| **Sample C** | Low Quality / Noisy | Line breaking up, high noise floor | Low SNR (< 10 dB), unresolvable pitch tracks | 🟡 **INCONCLUSIVE** | **MEDIUM** |
| **Sample D** | Voice + Financial Coercion | *"Transfer ₹50,000 immediately via UPI right now!"* | Financial keywords (`₹50,000`, `UPI`, `transfer`, `immediately`) | ⚠️ **FINANCIAL ALERT** | **HIGH** |

### Automated Test Suite
To verify the entire backend ML and risk decision pipeline:
```bash
python backend/test_backend.py
```
*Result: `ALL 4 BENCHMARK TESTS PASSED WITH 100% ACCURACY!`*

---

## 🏃 Running the Python FastAPI Backend

To run the optional server backend for API inspection or judge testing:
```bash
# 1. Install dependencies
pip install -r backend/requirements.txt

# 2. Start server
python backend/main.py
```
Server runs on: `http://localhost:8000`  
Interactive Swagger Docs: `http://localhost:8000/docs`

---

## 👥 Hackathon Team Assignments

- **ANUJ (Lead)**: Mobile UI/UX, Android APK pipeline, Live Call mode, Trust Report.
- **DEVELOPER 2**: Audio feature extraction (`F0`, Jitter, Spectral Centroid), Forensic classification.
- **DEVELOPER 3**: FastAPI endpoints (`/analyze`), Financial context engine, Deterministic risk rules.
