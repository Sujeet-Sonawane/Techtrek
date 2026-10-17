# NEXUS — GitHub Project Issues Tracker
**TechTrek 2026 Hackathon**
**Theme:** AI-Powered Fraud Prevention & Financial Security
**Tagline:** *Don't Trust the Voice. Verify the Request.*

---

## 👥 Team Assignments & Ownership
- **ANUJ (Lead)**: Frontend / Mobile UI/UX / APK Build Pipeline / Final Demo Rehearsal
- **DEVELOPER 2**: AI Engine / Audio Processing / Feature Extraction / Demo Samples
- **DEVELOPER 3**: Backend / FastAPI / Risk Decision Engine / Financial Context / Integration

---

## 📌 P0 — Core MVP (Hours 0:00 – 1:30)

### Issue #1: Repository Architecture Baseline & Workspace Setup
- **Assignee**: ALL
- **Priority**: P0
- **Labels**: `architecture`, `setup`
- **Description**: Establish multi-tier workspace layout: `android/` (Mobile app & APK pipeline), `backend/` (FastAPI services & ML models), `samples/` (Benchmark audio samples), and project documentation.
- **Acceptance Criteria**:
  - Directory structure initialized and tracked in Git.
  - Python virtual environment & dependencies verified.
  - Android Gradle wrapper and SDK paths confirmed.

---

### Issue #2: Mobile App Foundation & Android APK Pipeline
- **Assignee**: ANUJ
- **Priority**: P0
- **Labels**: `mobile`, `android`, `apk`
- **Description**: Setup native Android Jetpack Compose + WebView mobile shell with deep dark charcoal theme (`#0B0F17`), electric trust blue (`#2563EB`), and full touch-first layout targeting 360px–412px portrait viewports.
- **Acceptance Criteria**:
  - Android Gradle builds runnable `app-debug.apk` without errors.
  - AndroidManifest configured with microphone and audio permissions.
  - Standalone offline runtime enabled via local assets.

---

### Issue #3: Audio Input & Sample Selection System
- **Assignee**: ANUJ
- **Priority**: P0
- **Labels**: `mobile`, `audio`, `ui`
- **Description**: Build mobile audio selection interface allowing users to:
  1. Pick local `.wav` / `.mp3` files via Android system picker.
  2. Record live speech with Android microphone.
  3. One-tap select preloaded hackathon benchmark demo samples (Samples A, B, C, D).
- **Acceptance Criteria**:
  - Display selected file metadata: filename, duration, format, quality indicator.
  - Big tactile "Analyze Voice" CTA button.

---

### Issue #4: Audio Validation & Preprocessing Pipeline
- **Assignee**: DEVELOPER 2
- **Priority**: P0
- **Labels**: `ai`, `audio`, `validation`
- **Description**: Guardrail checks for uploaded audio files to prevent crashes and identify unclassifiable inputs before running heavy inference.
- **Acceptance Criteria**:
  - Detect empty, silent (< -50dB), or corrupted audio.
  - Enforce minimum duration check (> 0.8s required for pitch analysis).
  - Return graceful error responses (`"Recording too short or noisy for reliable analysis"`).

---

### Issue #5: Voice Authenticity Feature Extraction Engine
- **Assignee**: DEVELOPER 2
- **Priority**: P0
- **Labels**: `ai`, `audio`, `features`
- **Description**: Extract acoustic and spectral bio-markers to distinguish genuine biological speech from synthetic/cloned speech:
  - Fundamental pitch variation & jitter (F0 standard deviation).
  - Spectral centroid & spectral rolloff (vocoder smoothing artifacts).
  - Spectral flatness & zero-crossing rate (loss of natural micro-tremor).
- **Acceptance Criteria**:
  - Extracted feature vector returned in under 200ms per 5s audio chunk.
  - Runs in both Python backend (`scipy`/`numpy`) and local JavaScript WebAudio engine for offline fallback.

---

### Issue #6: Voice Authenticity Classification Engine
- **Assignee**: DEVELOPER 2
- **Priority**: P0
- **Labels**: `ai`, `classification`
- **Description**: Multi-class classification logic outputting:
  - `LIKELY GENUINE` (Natural pitch fluctuations, normal formant transitions, organic spectral energy).
  - `SUSPICIOUS` (Robotic pitch constancy, vocoder band artifacts, phase discontinuity).
  - `INCONCLUSIVE` (Extreme noise floor, low SNR, clipped amplitudes).
- **Acceptance Criteria**:
  - Honest uncertainty communication (no false "100% Human" claims).
  - Includes `evidence_strength` (`Strong`, `Moderate`, `Low`).

---

### Issue #7: FastAPI `/analyze` Endpoint & API Contract
- **Assignee**: DEVELOPER 3
- **Priority**: P0
- **Labels**: `backend`, `api`
- **Description**: Implement asynchronous FastAPI REST API accepting multipart audio uploads and JSON analysis payloads.
- **Acceptance Criteria**:
  - Endpoints: `GET /health`, `POST /analyze`, `GET /samples`.
  - JSON response conforms strictly to specification:
    ```json
    {
      "classification": "Suspicious",
      "risk_level": "HIGH",
      "evidence_strength": "Moderate",
      "audio_quality": "Good",
      "signals": ["Unusual spectral characteristics", "Low natural pitch variation"],
      "financial_context": true,
      "explanation": "...",
      "recommendation": "..."
    }
    ```

---

### Issue #8: Risk Assessment Engine
- **Assignee**: DEVELOPER 3
- **Priority**: P0
- **Labels**: `backend`, `risk-engine`
- **Description**: Combine Voice Authenticity classification with Conversation Context to compute holistic risk level:
  - `LOW`: Genuine voice + Non-financial context.
  - `MEDIUM`: Inconclusive voice OR Genuine voice with unverified financial request.
  - `HIGH`: Suspicious voice + Any context, OR Suspicious voice + Financial request.
- **Acceptance Criteria**:
  - No arbitrary pseudo-probabilities (e.g. avoid fake "94.73%").
  - Deterministic, explainable safety rules.

---

### Issue #9: Financial Context & Sensitive Keyword Detection
- **Assignee**: DEVELOPER 3
- **Priority**: P0
- **Labels**: `backend`, `nlp`, `security`
- **Description**: Analyze transcript / audio cues for high-risk financial markers:
  - Currency: `₹`, `Rupees`, `INR`, `transfer`, `payment`, `send`.
  - Payment rails: `UPI`, `GPay`, `PhonePe`, `Paytm`, `NEFT`, `RTGS`, `IMPS`.
  - Credentials: `OTP`, `PIN`, `password`, `CVV`, `card number`.
  - Identity & Banking: `Bank`, `Account`, `KYC`, `Aadhaar`, `PAN`, `manager`.
  - Urgency triggers: `Immediately`, `Urgent`, `Right now`, `Emergency`.
- **Acceptance Criteria**:
  - Returns `financial_context: true/false` and list of detected financial triggers.
  - Explicit non-accusatory messaging: `"Sensitive financial request detected"`.

---

### Issue #10: Safety Guidance & Verification Checklist
- **Assignee**: ANUJ & DEVELOPER 3
- **Priority**: P0
- **Labels**: `mobile`, `security`, `ux`
- **Description**: Create dynamic Action Banner and "How Should I Verify?" modal checklist:
  - 🛑 Stop and verify before sending money.
  - 📞 Call back on known personal phone number (not the incoming call line).
  - 💬 Cross-verify on separate channel (e.g. official email or office messaging).
  - 🔒 Never share OTP, PIN, password, or banking credentials under urgency.
- **Acceptance Criteria**:
  - High-visibility banner when risk is HIGH or financial context is detected.
  - Interactive tap-to-expand checklist.

---

### Issue #11: Mobile Result Screen & Human Explainability
- **Assignee**: ANUJ
- **Priority**: P0
- **Labels**: `mobile`, `ui`, `explainability`
- **Description**: Implement consumer-grade Result Screen with clear visual hierarchy:
  1. Voice Authenticity Badge (🟢 Genuine / 🟡 Inconclusive / 🔴 Suspicious).
  2. Risk Level Indicator (LOW / MEDIUM / HIGH).
  3. Evidence Strength & Audio Quality chips.
  4. Detected Signals bullet list.
  5. Financial Context Banner (if detected).
  6. Plain-language Human Explanation.
  7. "View Technical Details" collapsible section for judges.
- **Acceptance Criteria**:
  - Tested on 360px–412px screen widths with zero overflow.
  - High contrast, legible typography, no neon clutter.

---

### Issue #12: End-to-End Integration & Resilient Offline Fallback
- **Assignee**: ALL
- **Priority**: P0
- **Labels**: `integration`, `reliability`
- **Description**: Connect Mobile App to FastAPI backend with zero-latency automatic offline fallback if network is unavailable.
- **Acceptance Criteria**:
  - App checks backend `/health` on startup.
  - Seamlessly performs full acoustic and risk analysis locally if offline.
  - Judge demo runs without internet connection.

---

## 🚀 P1 — Innovation Features (Hours 1:30 – 2:30)

### Issue #13: Live Call Demo Simulation Mode
- **Assignee**: ANUJ & DEVELOPER 2
- **Priority**: P1
- **Labels**: `innovation`, `live-demo`
- **Description**: Build interactive Live Call screen simulating real-time call monitoring:
  - Caller header: "Rahul Sharma — Manager".
  - Live call timer (`00:00` -> `00:45`).
  - Real-time animated audio waveform visualizer.
  - Live Voice Authenticity indicator updating dynamically.
  - Triggered voice alert: `"⚠️ VOICE ALERT: Suspicious voice characteristics detected"`.
  - Triggered financial alert: `"🚨 FINANCIAL REQUEST DETECTED: Verify independently before taking action"`.
- **Acceptance Criteria**:
  - Technically credible: Controlled audio stream / simulated VoIP stream.
  - Dynamic alert triggers synced to audio events.

---

### Issue #14: Real-Time Authenticity Timeline with Explainable Segments
- **Assignee**: ANUJ & DEVELOPER 2
- **Priority**: P1
- **Labels**: `innovation`, `timeline`, `xai`
- **Description**: Interactive timeline strip displaying 5-second interval markers:
  - `00:00 🟢 LOW`
  - `00:10 🟢 LOW`
  - `00:20 🟡 MEDIUM`
  - `00:30 🔴 HIGH`
  - Tapping any segment opens bottom-sheet explaining exact signal detected at that timestamp.
- **Acceptance Criteria**:
  - Tap interaction reveals segment-specific acoustic signals.
  - Visual status chips with high accessibility contrast.

---

### Issue #15: Post-Call Trust Report Generation
- **Assignee**: ANUJ
- **Priority**: P1
- **Labels**: `innovation`, `report`
- **Description**: Comprehensive executive summary modal displayed upon ending the Live Call:
  - Call Duration & Caller details.
  - Overall Authenticity, Risk, Evidence Strength, Audio Quality.
  - Comprehensive "Why was this flagged?" breakdown.
  - Actionable Recommended Next Steps for the user.
- **Acceptance Criteria**:
  - One-tap "Save/Share Report" or "Analyze Another Audio".
  - Polished executive design for judges.

---

## 🔬 P2 — Optional Enhancements (Hours 2:30 – 3:00)

### Issue #16: Active Voice Verification Challenge (Liveness Protocol)
- **Assignee**: DEVELOPER 2 & ANUJ
- **Priority**: P2
- **Labels**: `optional`, `anti-replay`
- **Description**: Generate dynamic challenge phrase (`"Please say: My verification code is 4827"`) to test caller responsiveness and defeat pre-recorded deepfake replays.
- **Acceptance Criteria**:
  - Clean UI card with challenge phrase generator.
  - Verification response timer.
