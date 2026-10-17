"""
NEXUS Backend API Server
FastAPI server providing Voice Authenticity Forensics, Financial Context Detection,
and Defined Risk Guidance for Mobile and Web clients.
"""

import os
from typing import Optional, List
from fastapi import FastAPI, File, UploadFile, Form, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse, JSONResponse
from pydantic import BaseModel

from audio_analyzer import analyze_audio_forensics
from financial_detector import detect_financial_context
from risk_engine import evaluate_risk

app = FastAPI(
    title="NEXUS Voice Authenticity & Financial Safety API",
    description="Don't Trust the Voice. Verify the Request. (TechTrek 2026)",
    version="1.0.0"
)

# Enable CORS for mobile apps, emulators, and local development
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

SAMPLES_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "samples")

DEMO_CATALOG = {
    "sample_a": {
        "id": "sample_a",
        "title": "Sample A — Genuine Speech",
        "description": "Clear natural vocal dynamics with human pitch micro-tremors.",
        "filename": "Sample_A_Genuine.wav",
        "transcript": "Hello, this is Rahul. Just following up on the quarterly roadmap review we discussed in yesterday's meeting.",
        "expected_classification": "LIKELY GENUINE"
    },
    "sample_b": {
        "id": "sample_b",
        "title": "Sample B — Suspicious (Cloned Speech)",
        "description": "Synthetic speech exhibiting pitch constancy and vocoder spectral artifacts.",
        "filename": "Sample_B_Suspicious_Cloned.wav",
        "transcript": "Hello, this is Rahul. I am currently out of office. Please process the pending system invoice today.",
        "expected_classification": "SUSPICIOUS"
    },
    "sample_c": {
        "id": "sample_c",
        "title": "Sample C — Inconclusive (High Noise)",
        "description": "Short, low-SNR noisy recording with unresolvable pitch tracks.",
        "filename": "Sample_C_Inconclusive_Noisy.wav",
        "transcript": "Hello... can you hear me... line is breaking up...",
        "expected_classification": "INCONCLUSIVE"
    },
    "sample_d": {
        "id": "sample_d",
        "title": "Sample D — Urgent Financial Request",
        "description": "Voice call requesting an immediate ₹50,000 transfer to an unverified UPI account.",
        "filename": "Sample_D_Financial_Urgent_Transfer.wav",
        "transcript": "Rahul here! I am stuck in an emergency meeting. Transfer ₹50,000 immediately to the UPI ID sent via SMS right now!",
        "expected_classification": "SUSPICIOUS"
    }
}


class AnalysisResponse(BaseModel):
    valid: bool
    classification: str
    risk_level: str
    evidence_strength: str
    audio_quality: str
    signals: List[str]
    financial_context: bool
    financial_triggers: List[str]
    urgency_detected: bool
    explanation: str
    recommendation: str
    verification_steps: List[str]
    duration_seconds: Optional[float] = 0.0
    f0_std: Optional[float] = None
    jitter_pct: Optional[float] = None
    spectral_centroid: Optional[float] = None
    disclaimer: str


@app.get("/health")
def health_check():
    return {
        "status": "online",
        "service": "NEXUS Voice Authenticity API",
        "version": "1.0.0",
        "hackathon": "TechTrek 2026"
    }


@app.get("/samples")
def get_demo_samples():
    return list(DEMO_CATALOG.values())


@app.get("/samples/{sample_id}/audio")
def get_sample_audio(sample_id: str):
    sample = DEMO_CATALOG.get(sample_id.lower())
    if not sample:
        raise HTTPException(status_code=404, detail="Sample not found")
    
    file_path = os.path.join(SAMPLES_DIR, sample["filename"])
    if not os.path.exists(file_path):
        raise HTTPException(status_code=404, detail="Audio file not found on disk")
        
    return FileResponse(file_path, media_type="audio/wav", filename=sample["filename"])


@app.post("/analyze", response_model=AnalysisResponse)
async def analyze_audio(
    file: Optional[UploadFile] = File(None),
    sample_id: Optional[str] = Form(None),
    transcript: Optional[str] = Form(None)
):
    """
    Primary endpoint: Analyzes an uploaded audio recording or chosen benchmark sample.
    """
    audio_bytes = b""
    filename = "recording.wav"
    input_text = transcript or ""

    if sample_id and sample_id.lower() in DEMO_CATALOG:
        sample = DEMO_CATALOG[sample_id.lower()]
        file_path = os.path.join(SAMPLES_DIR, sample["filename"])
        if os.path.exists(file_path):
            with open(file_path, "rb") as f:
                audio_bytes = f.read()
            filename = sample["filename"]
            if not input_text:
                input_text = sample["transcript"]
    elif file:
        audio_bytes = await file.read()
        filename = file.filename or "uploaded.wav"
    else:
        raise HTTPException(status_code=400, detail="Must provide either an audio file or a valid sample_id.")

    if len(audio_bytes) == 0:
        raise HTTPException(status_code=400, detail="Audio payload is empty.")

    # 1. Acoustic Forensics Analysis (Issues #12, #13, #14)
    forensic_result = analyze_audio_forensics(audio_bytes, filename)

    # 2. Financial Context Detection
    financial_result = detect_financial_context(input_text)

    # 3. Holistic Risk Assessment
    risk_result = evaluate_risk(
        voice_classification=forensic_result["classification"],
        evidence_strength=forensic_result["evidence_strength"],
        financial_context=financial_result["financial_context"],
        urgency_detected=financial_result["urgency_detected"],
        detected_signals=forensic_result["signals"]
    )

    # Combine signals
    combined_signals = list(forensic_result["signals"])
    if financial_result["financial_context"]:
        combined_signals.append(f"Financial keywords: {', '.join(financial_result['financial_triggers'])}")
    if financial_result["urgency_detected"]:
        combined_signals.append("High-pressure urgency markers detected")

    return AnalysisResponse(
        valid=forensic_result["valid"],
        classification=forensic_result["classification"],
        risk_level=risk_result["risk_level"],
        evidence_strength=forensic_result["evidence_strength"],
        audio_quality=forensic_result["audio_quality"],
        signals=combined_signals,
        financial_context=financial_result["financial_context"],
        financial_triggers=financial_result["financial_triggers"],
        urgency_detected=financial_result["urgency_detected"],
        explanation=risk_result["explanation"],
        recommendation=risk_result["recommendation"],
        verification_steps=risk_result["verification_steps"],
        duration_seconds=forensic_result.get("duration_seconds", 0.0),
        f0_std=forensic_result.get("f0_std"),
        jitter_pct=forensic_result.get("jitter_pct"),
        spectral_centroid=forensic_result.get("spectral_centroid"),
        disclaimer=risk_result["disclaimer"]
    )


# ==============================================================================
# Issue #15: Real-Time Authenticity Timeline Endpoint
# ==============================================================================
@app.post("/analyze-timeline")
async def analyze_timeline_endpoint(
    file: Optional[UploadFile] = File(None),
    sample_id: Optional[str] = Form(None)
):
    """
    Breaks audio into time slices and produces an explainable authenticity timeline.
    """
    from audio_analyzer import generate_authenticity_timeline
    
    audio_bytes = b""
    if sample_id and sample_id.lower() in DEMO_CATALOG:
        sample = DEMO_CATALOG[sample_id.lower()]
        file_path = os.path.join(SAMPLES_DIR, sample["filename"])
        if os.path.exists(file_path):
            with open(file_path, "rb") as f:
                audio_bytes = f.read()
    elif file:
        audio_bytes = await file.read()
    else:
        raise HTTPException(status_code=400, detail="Missing audio input or sample_id")

    timeline = generate_authenticity_timeline(audio_bytes, window_sec=1.5)
    return {"timeline": timeline}


# ==============================================================================
# Issue #16: Active Voice Verification Challenge (Anti-Replay)
# ==============================================================================
@app.get("/challenge/generate")
def generate_voice_challenge():
    """
    Generates a dynamic challenge phrase and security code to defeat deepfake replays.
    """
    from audio_analyzer import ActiveVoiceChallengeEngine
    return ActiveVoiceChallengeEngine.generate_challenge()


@app.post("/challenge/verify")
async def verify_voice_challenge(
    expected_code: str = Form(...),
    file: Optional[UploadFile] = File(None)
):
    """
    Validates caller's spoken response for liveness and active responsiveness.
    """
    from audio_analyzer import ActiveVoiceChallengeEngine
    
    if not file:
        raise HTTPException(status_code=400, detail="Missing challenge response audio")
        
    audio_bytes = await file.read()
    result = ActiveVoiceChallengeEngine.verify_liveness(audio_bytes, expected_code)
    return result


@app.post("/analyze-stream")
async def analyze_stream_chunk(
    timestamp: float = Form(...),
    transcript_segment: str = Form(""),
    is_suspicious_audio: bool = Form(False)
):
    """
    Lightweight streaming hook for real-time live call monitoring simulation.
    """
    financial_data = detect_financial_context(transcript_segment)
    
    if is_suspicious_audio:
        classification = "SUSPICIOUS"
        risk_level = "HIGH" if financial_data["financial_context"] else "MEDIUM"
        signal = "Unusual spectral uniformity detected in stream segment"
    elif financial_data["financial_context"]:
        classification = "LIKELY GENUINE"
        risk_level = "HIGH" if financial_data["urgency_detected"] else "MEDIUM"
        signal = "Sensitive financial intent detected in stream audio"
    else:
        classification = "LIKELY GENUINE"
        risk_level = "LOW"
        signal = "Natural vocal harmonics"

    return {
        "timestamp": timestamp,
        "classification": classification,
        "risk_level": risk_level,
        "signal": signal,
        "financial_context": financial_data["financial_context"],
        "triggers": financial_data["financial_triggers"]
    }


if __name__ == "__main__":
    import uvicorn
    print("Starting NEXUS Backend on http://0.0.0.0:8000")
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=False)
