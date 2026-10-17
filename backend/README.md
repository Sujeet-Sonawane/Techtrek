# NEXUS Backend & Risk Decision Engine

**Theme:** AI-Powered Fraud Prevention & Financial Security  
**Tagline:** *Don't Trust the Voice. Verify the Request.*  
**Owner / Backend Lead:** Sujeet Sonawane ([@Sujeet-Sonawane](https://github.com/Sujeet-Sonawane))

---

## 📋 Assigned Tasks & Deliverables

| Issue | Title | Status | Description |
|---|---|---|---|
| **#1** | **Repository Architecture Baseline** | ✅ Completed | Setup directory hierarchy (`android/`, `backend/`, `samples/`), dependencies, and test suite. |
| **#2** | **FastAPI `/analyze` Endpoint & API Contract** | ✅ Completed | Asynchronous REST API accepting multipart audio uploads and sample IDs matching the strict NEXUS schema. |
| **#3** | **Risk Assessment Engine** | ✅ Completed | Deterministic rule-based risk evaluation combining voice classification and conversational financial context into `LOW`, `MEDIUM`, or `HIGH`. |
| **#4** | **Financial Context & Keyword Detection** | ✅ Completed | Regex and NLP pattern engine detecting Indian/global currencies, UPI rails, credentials (OTP, PIN), and urgency triggers. |
| **#5** | **End-to-End Integration & Resilient Offline Fallback** | ✅ Completed | Seamless contract parity allowing mobile client (`android/app/src/main/assets/`) to execute both against live FastAPI or local offline WebAudio engine. |

---

## 🚀 Running the Backend

### 1. Install Dependencies
```bash
pip install -r backend/requirements.txt
```

### 2. Run the Verification Tests
```bash
# Run audio forensics verification
python backend/test_backend.py

# Run full FastAPI & Risk Engine integration suite
python backend/test_api.py
```

### 3. Start the Server
```bash
uvicorn backend.main:app --host 0.0.0.0 --port 8000 --reload
```
Interactive Swagger API documentation available at: `http://localhost:8000/docs`

---

## 📡 API Contract Specification

### `GET /health`
Returns health check and version metadata.
```json
{
  "status": "online",
  "service": "NEXUS Voice Authenticity API",
  "version": "1.0.0",
  "hackathon": "TechTrek 2026"
}
```

### `GET /samples`
Returns the catalog of 4 preloaded hackathon benchmark audio samples (Samples A, B, C, D).

### `POST /analyze`
Accepts `multipart/form-data`:
- `file`: (Optional) Uploaded `.wav` / `.mp3` audio file.
- `sample_id`: (Optional) e.g., `"sample_a"`, `"sample_b"`, `"sample_c"`, `"sample_d"`.
- `transcript`: (Optional) Audio transcript text for financial keyword extraction.

**Response Schema:**
```json
{
  "valid": true,
  "classification": "SUSPICIOUS",
  "risk_level": "HIGH",
  "evidence_strength": "High",
  "audio_quality": "Good",
  "signals": [
    "Reduced natural pitch variation (mechanical constancy)",
    "Financial keywords: UPI Rail, Funds Transfer",
    "High-pressure urgency markers detected"
  ],
  "financial_context": true,
  "financial_triggers": ["UPI Rail", "Funds Transfer"],
  "urgency_detected": true,
  "explanation": "The recording contains characteristics consistent with synthetic or manipulated speech...",
  "recommendation": "DO NOT PROCEED. Verify the request through an independent trusted channel...",
  "verification_steps": [
    "Stop and pause before taking any financial action.",
    "Contact the alleged caller directly using a previously saved, trusted phone number.",
    "Confirm the request through an independent communication channel.",
    "Never disclose OTPs, UPI PINs, passwords, or banking credentials over the phone.",
    "Verify the beneficiary account details independently before initiating transfers."
  ],
  "duration_seconds": 3.42,
  "f0_std": 6.8,
  "spectral_centroid": 2450.1,
  "disclaimer": "Risk level is based on detected acoustic signals and established financial safety rules; it is not an arbitrary statistical probability."
}
```

---

## 🛡️ Risk Assessment Decision Matrix (Issue #3)

| Voice Classification | Financial Request Detected? | Urgency Pressure Detected? | Holistic Risk Level | Action Guidance |
|---|---|---|---|---|
| **SUSPICIOUS** | Yes / No | Yes / No | **🔴 HIGH** | **DO NOT PROCEED**. Verify caller identity via alternative channel. |
| **INCONCLUSIVE** | Yes | Any | **🔴 HIGH** | **VERIFY BEFORE YOU ACT**. Unverified voice asking for funds. |
| **INCONCLUSIVE** | No | Any | **🟡 MEDIUM** | **REQUEST CLEARER AUDIO**. Inconclusive acoustic properties. |
| **LIKELY GENUINE** | Yes | Yes (Urgent) | **🔴 HIGH** | **VERIFY BEFORE YOU ACT**. Dual-channel authorization mandatory. |
| **LIKELY GENUINE** | Yes | No | **🟡 MEDIUM** | **CONFIRM INDEPENDENTLY**. Financial activity detected. |
| **LIKELY GENUINE** | No | No | **🟢 LOW** | **NO IMMEDIATE CONCERN**. Organic voice acoustics observed. |

---

## 🇮🇳 Financial & Urgency Keywords Detected (Issue #4)

- **Currencies & Amounts**: `₹`, `Rupees`, `INR`, `Lakh`, `Crore`, `transfer`, `payment`, `send money`.
- **Payment Rails**: `UPI`, `GPay` / `Google Pay`, `PhonePe`, `Paytm`, `NEFT`, `RTGS`, `IMPS`, `Bank Account`.
- **Credentials & Security**: `OTP`, `UPI PIN`, `PIN`, `Password`, `CVV`, `Card number`.
- **Identity / Compliance**: `KYC`, `Aadhaar`, `PAN`, `Bank Manager`, `Beneficiary`.
- **Urgency Triggers**: `Immediately`, `Right now`, `Urgent`, `Emergency`, `Account blocked/frozen`, `Customs/Police`.
