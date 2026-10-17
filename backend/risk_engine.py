"""
NEXUS Holistic Risk Engine
Combines Voice Authenticity with Conversation Context and Defined Safety Rules.
"""

from typing import Dict, Any, List

VERIFICATION_STEPS = [
    "Stop and pause before taking any financial action.",
    "Contact the alleged caller directly using a previously saved, trusted phone number.",
    "Confirm the request through an independent communication channel (e.g., official corporate email or in-person).",
    "Never disclose OTPs, UPI PINs, passwords, or banking credentials over the phone.",
    "Verify the beneficiary account details independently before initiating transfers."
]


def evaluate_risk(voice_classification: str,
                  evidence_strength: str,
                  financial_context: bool,
                  urgency_detected: bool,
                  detected_signals: List[str]) -> Dict[str, Any]:
    """
    Evaluates holistic risk and generates safety recommendations.
    """
    v_class = voice_classification.upper()

    # Rule-Based Risk Matrix
    if v_class == "SUSPICIOUS":
        if financial_context or urgency_detected:
            risk_level = "HIGH"
            recommendation = "DO NOT PROCEED. Verify the request through an independent trusted channel before taking financial action."
            explanation = (
                "The recording contains characteristics consistent with synthetic or manipulated speech. "
                "Because this conversation also involves a sensitive financial request or urgent transfer, "
                "immediate independent verification is strongly recommended."
            )
        else:
            risk_level = "HIGH"
            recommendation = "VERIFY IDENTITY. Suspicious synthetic speech signals detected."
            explanation = (
                "The audio displays acoustic anomalies characteristic of voice cloning or speech synthesis. "
                "Exercise caution and verify the identity of the speaker."
            )

    elif v_class == "INCONCLUSIVE":
        if financial_context:
            risk_level = "HIGH"
            recommendation = "VERIFY BEFORE YOU ACT. Unverified voice requesting financial transfer."
            explanation = (
                "The voice authenticity could not be definitively validated due to audio quality or length. "
                "Because a financial request was made, treat this call with caution and independently verify the requester."
            )
        else:
            risk_level = "MEDIUM"
            recommendation = "REQUEST CLEARER AUDIO. Inconclusive authenticity."
            explanation = (
                "Acoustic features were insufficient to confirm whether this voice is genuine or synthetic. "
                "Obtain a clearer recording or confirm identity via alternative channels."
            )

    else:  # LIKELY GENUINE
        if financial_context:
            if urgency_detected:
                risk_level = "HIGH"
                recommendation = "VERIFY BEFORE YOU ACT. High-urgency financial request."
                explanation = (
                    "Although the voice characteristics appear natural, high-urgency financial transactions "
                    "require dual-channel authorization. The speaker may be compromised, coerced, or spoofing authority."
                )
            else:
                risk_level = "MEDIUM"
                recommendation = "CONFIRM TRANSACTION INDEPENDENTLY. Financial activity detected."
                explanation = (
                    "Voice acoustic dynamics appear consistent with authentic human speech. "
                    "However, standard financial security protocol mandates independent confirmation of any monetary transfer."
                )
        else:
            risk_level = "LOW"
            recommendation = "NO IMMEDIATE SAFETY CONCERN IDENTIFIED."
            explanation = (
                "The voice shows natural acoustic inflections and pitch variation, and no sensitive financial "
                "or urgent requests were detected in the conversation."
            )

    return {
        "risk_level": risk_level,
        "recommendation": recommendation,
        "explanation": explanation,
        "verification_steps": VERIFICATION_STEPS,
        "disclaimer": "Risk level is based on detected acoustic signals and established financial safety rules; it is not an arbitrary statistical probability."
    }
