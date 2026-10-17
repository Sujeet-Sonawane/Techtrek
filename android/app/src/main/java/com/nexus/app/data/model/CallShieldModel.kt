package com.nexus.app.data.model

enum class CallSecurityStatus(
    val title: String,
    val subtitle: String,
    val isRed: Boolean,
    val isGreen: Boolean,
    val isAmber: Boolean
) {
    IDLE(
        "Call Guard Active",
        "Monitoring incoming calls for synthetic voice cloning and financial scams.",
        isRed = false, isGreen = true, isAmber = false
    ),
    ANALYZING(
        "Listening & Verifying Voice Stream...",
        "Measuring vocal biomechanics, acoustic jitter, and conversational risk signals.",
        isRed = false, isGreen = false, isAmber = true
    ),
    AI_CLONE_DETECTED(
        "AI Cloned Voice Detected",
        "High confidence of synthetic neural speech synthesis. DO NOT transfer money or share OTPs.",
        isRed = true, isGreen = false, isAmber = false
    ),
    GENUINE_HUMAN_VOICE(
        "Genuine Human Voice Verified",
        "Natural vocal cord micro-tremors, continuous harmonic frequencies, and organic breathing detected.",
        isRed = false, isGreen = true, isAmber = false
    ),
    SUSPICIOUS_FINANCIAL_URGENCY(
        "High Financial Urgency Warning",
        "Caller is using aggressive social engineering tactics demanding immediate payment/action.",
        isRed = false, isGreen = false, isAmber = true
    )
}

data class CallShieldState(
    val isActiveCall: Boolean = false,
    val isRinging: Boolean = false,
    val callerNumber: String = "+91 98201 44821",
    val callerName: String = "Unknown Caller",
    val callerLabel: String = "Suspected Voice Clone",
    val status: CallSecurityStatus = CallSecurityStatus.IDLE,
    val liveJitterPercent: Float = 0.82f,
    val livePitchHz: Float = 145.0f,
    val liveRmsEnergy: Float = 0.45f,
    val syntheticLikelihood: Float = 0.05f,
    val detectedKeywords: List<String> = emptyList(),
    val alertHeadline: String = "Call Guard Ready",
    val safetyAdvice: String = "Keep Call Guard enabled to screen incoming cellular and WhatsApp audio calls.",
    val liveTranscript: String = "",
    val explainabilityFactors: List<ForensicExplainabilityFactor> = emptyList()
)

data class CallSimulationPreset(
    val id: String,
    val title: String,
    val callerName: String,
    val callerNumber: String,
    val callerType: String,
    val targetStatus: CallSecurityStatus,
    val speechTranscript: String,
    val detectedKeywords: List<String>,
    val syntheticProbability: Float,
    val jitter: Float,
    val pitch: Float,
    val safetyAdvice: String,
    val factors: List<ForensicExplainabilityFactor>
)

object CallSimulationPresets {
    val presets: List<CallSimulationPreset> = listOf(
        // Preset 1: Urgent Manager Clone
        CallSimulationPreset(
            id = "sim_manager_clone",
            title = "Manager Voice Clone (Urgent ₹50,000 NEFT)",
            callerName = "Executive Director / Reporting Manager",
            callerNumber = "+91 98201 55902",
            callerType = "AI VOICE CLONE SCAM",
            targetStatus = CallSecurityStatus.AI_CLONE_DETECTED,
            speechTranscript = "Hey, I'm stuck in an emergency board meeting. Transfer 50,000 rupees to this vendor UPI right now, I will reimburse you by evening.",
            detectedKeywords = listOf("emergency", "transfer 50,000 rupees", "vendor UPI", "right now"),
            syntheticProbability = 0.94f,
            jitter = 0.06f,
            pitch = 120.0f,
            safetyAdvice = "IMMEDIATE ACTION: Do not transfer funds. Hang up and verify identity on known SIM phone number.",
            factors = listOf(
                ForensicExplainabilityFactor(
                    factorName = "Vocal Cord Micro-Tremor (Jitter)",
                    observedValue = "0.06% (Unnaturally Low)",
                    expectedNormalRange = "0.40% - 1.20%",
                    isSuspicious = true,
                    impactWeight = "+40% Suspicion",
                    diagnosticSummary = "Absence of natural neuromuscular vocal micro-tremors indicates mathematical neural vocoder synthesis.",
                    technicalRationale = "Human larynx muscles involuntarily oscillate between 0.4% and 1.2% jitter. Generative diffusion models smooth this out to <0.1%."
                ),
                ForensicExplainabilityFactor(
                    factorName = "Speech Pause Acoustic Floor",
                    observedValue = "0 dB (Dead Digital Silence)",
                    expectedNormalRange = "-45 dB to -30 dB (Room ambience)",
                    isSuspicious = true,
                    impactWeight = "+25% Suspicion",
                    diagnosticSummary = "Pauses between words contain zero acoustic ambient room noise.",
                    technicalRationale = "Generated speech clips insert digital zeros between synthesis chunks rather than capturing room reverberation."
                ),
                ForensicExplainabilityFactor(
                    factorName = "Social Engineering Urgency",
                    observedValue = "Critical Urgency (Immediate payment)",
                    expectedNormalRange = "Standard business communication",
                    isSuspicious = true,
                    impactWeight = "+20% Risk",
                    diagnosticSummary = "Detected coercive language pressuring recipient to bypass standard corporate approval.",
                    technicalRationale = "Matches typical CEO fraud / authority impersonation vector."
                )
            )
        ),

        // Preset 2: Digital Arrest Police Threat
        CallSimulationPreset(
            id = "sim_digital_arrest",
            title = "Digital Arrest Scam (Fake Police Officer)",
            callerName = "Cyber Crime Branch / Delhi Police HQ",
            callerNumber = "+91 11 2345 6789",
            callerType = "COERCIVE EXTORTION",
            targetStatus = CallSecurityStatus.AI_CLONE_DETECTED,
            speechTranscript = "This is Officer Sharma from Delhi Police HQ. Your Aadhaar is implicated in money laundering and narcotics parcel. You are under digital arrest. Pay security bond immediately.",
            detectedKeywords = listOf("Delhi Police", "narcotics parcel", "digital arrest", "pay security bond"),
            syntheticProbability = 0.91f,
            jitter = 0.08f,
            pitch = 95.0f,
            safetyAdvice = "CRITICAL WARNING: 'Digital Arrest' does not exist under Indian law. Disconnect and call 1930 immediately.",
            factors = listOf(
                ForensicExplainabilityFactor(
                    factorName = "Monotonic Pitch Contour (Prosody)",
                    observedValue = "Pitch Variance < 8 Hz",
                    expectedNormalRange = "25 Hz - 60 Hz Dynamic",
                    isSuspicious = true,
                    impactWeight = "+35% Suspicion",
                    diagnosticSummary = "Robotic monotone delivery typical of automated IVR text-to-speech tools.",
                    technicalRationale = "Automated threatening script read by synthetic text-to-speech engine."
                ),
                ForensicExplainabilityFactor(
                    factorName = "Legal Coercion Pattern",
                    observedValue = "Extortion via 'Digital Arrest'",
                    expectedNormalRange = "Formal written police summons",
                    isSuspicious = true,
                    impactWeight = "+45% Fraud Risk",
                    diagnosticSummary = "Official police notices are NEVER served via phone or video call demanding funds.",
                    technicalRationale = "PIB and MHA have declared all 'Digital Arrest' calls as criminal fraud."
                )
            )
        ),

        // Preset 3: Authentic Family Call
        CallSimulationPreset(
            id = "sim_authentic_family",
            title = "Authentic Call (Family Member / Mom)",
            callerName = "Mom (Saved Contact)",
            callerNumber = "+91 94220 18432",
            callerType = "VERIFIED CONTACT",
            targetStatus = CallSecurityStatus.GENUINE_HUMAN_VOICE,
            speechTranscript = "Beta, did you reach safely? Please eat lunch on time and call me back when your meeting ends.",
            detectedKeywords = emptyList(),
            syntheticProbability = 0.03f,
            jitter = 0.78f,
            pitch = 210.0f,
            safetyAdvice = "SAFE: Natural acoustic harmonics and biological vocal tract parameters confirmed.",
            factors = listOf(
                ForensicExplainabilityFactor(
                    factorName = "Vocal Cord Micro-Tremor (Jitter)",
                    observedValue = "0.78% (Healthy Human Tremor)",
                    expectedNormalRange = "0.40% - 1.20%",
                    isSuspicious = false,
                    impactWeight = "-30% Suspicion (Natural)",
                    diagnosticSummary = "Organic biomechanical vocal tract vibration observed throughout phonation.",
                    technicalRationale = "Consistent with live human larynx mechanics and uncompressed organic speech."
                ),
                ForensicExplainabilityFactor(
                    factorName = "Harmonic Formant Continuity",
                    observedValue = "Natural F1-F4 Formant Glide",
                    expectedNormalRange = "Continuous formant trajectories",
                    isSuspicious = false,
                    impactWeight = "-25% Suspicion (Natural)",
                    diagnosticSummary = "No spectral phase slicing or concatenative join glitches detected.",
                    technicalRationale = "Vocal tract transitions glide smoothly according to physiological constraints."
                )
            )
        ),

        // Preset 4: Bank KYC Social Engineering
        CallSimulationPreset(
            id = "sim_bank_kyc",
            title = "Bank KYC Expiry (Urgent OTP Request)",
            callerName = "State Bank KYC Helpdesk",
            callerNumber = "+91 80012 34567",
            callerType = "SUSPICIOUS FINANCIAL URGENCY",
            targetStatus = CallSecurityStatus.SUSPICIOUS_FINANCIAL_URGENCY,
            speechTranscript = "Sir your bank account KYC is suspended. To reactivate please share the 6 digit OTP received on your mobile right now.",
            detectedKeywords = listOf("KYC suspended", "6 digit OTP", "mobile right now"),
            syntheticProbability = 0.65f,
            jitter = 0.35f,
            pitch = 155.0f,
            safetyAdvice = "NEVER SHARE OTP: Legitimate banks never call asking for OTP or NetBanking passwords.",
            factors = listOf(
                ForensicExplainabilityFactor(
                    factorName = "Social Engineering / Credential Theft",
                    observedValue = "Direct Request for OTP",
                    expectedNormalRange = "Zero OTP request policy",
                    isSuspicious = true,
                    impactWeight = "+50% Risk",
                    diagnosticSummary = "Caller explicitly demanded 6-digit OTP to 'reactivate' account.",
                    technicalRationale = "RBI guidelines strictly state banks never solicit OTPs over telephone calls."
                ),
                ForensicExplainabilityFactor(
                    factorName = "Voice Synthesis Probability",
                    observedValue = "Moderate / Telephony Compressed",
                    expectedNormalRange = "Indeterminate",
                    isSuspicious = false,
                    impactWeight = "+15% Risk",
                    diagnosticSummary = "Telephony bandpass filtering masks subtle vocal vocoder cues.",
                    technicalRationale = "Flagged primarily due to high risk credential harvesting."
                )
            )
        )
    )
}
