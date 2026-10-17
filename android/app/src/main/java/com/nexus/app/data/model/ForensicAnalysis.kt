package com.nexus.app.data.model

enum class ForensicVerdict(val label: String, val description: String) {
    SUSPICIOUS_INDICATORS_DETECTED(
        "Suspicious Indicators Detected",
        "Acoustic and/or visual anomalies suggest synthetic generation or splicing."
    ),
    NO_SUSPICIOUS_INDICATORS_DETECTED(
        "No Suspicious Indicators Detected",
        "Acoustic frequencies, natural vocal jitter, and continuity align with authentic recordings."
    ),
    INCONCLUSIVE(
        "Inconclusive",
        "Recording quality, background noise, or compression artifacts prevent definitive classification."
    ),
    MODEL_UNAVAILABLE(
        "Model Offline / Local Baseline Only",
        "Remote neural inference could not be reached. Evaluated against local forensic rules."
    )
}

enum class EvidenceConfidence(val label: String) {
    STRONG("Strong Evidence"),
    MODERATE("Moderate Evidence"),
    PRELIMINARY("Preliminary / Low Confidence")
}

data class TimestampedFinding(
    val timestampSeconds: Float,
    val formattedTime: String,
    val findingType: String,
    val description: String,
    val severity: String, // "High", "Moderate", "Info"
    val possibleCauses: String
)

data class AudioAuthenticity(
    val status: String,
    val confidence: EvidenceConfidence,
    val pitchVarianceHz: Float,
    val spectralCentroidHz: Float,
    val jitterPercent: Float,
    val syntheticLikelihood: String, // "High", "Moderate", "Low"
    val technicalNotes: String
)

data class AudioIntegrity(
    val clippingDetected: Boolean,
    val suddenTransitionsCount: Int,
    val snrDb: Float,
    val anomaliesFound: List<String>
)

data class VideoSync(
    val avOffsetMs: Int,
    val lipSyncAlignment: String, // "Synchronized", "Offset Detected", "Unnatural Phonation"
    val visualArtifactsDetected: Boolean,
    val visualNotes: String
)

data class ForensicAnalysis(
    val verdict: ForensicVerdict,
    val confidence: EvidenceConfidence,
    val headline: String,
    val summary: String,
    val audioAuthenticity: AudioAuthenticity,
    val audioIntegrity: AudioIntegrity,
    val videoSync: VideoSync? = null,
    val timelineFindings: List<TimestampedFinding> = emptyList(),
    val technicalProvenance: Map<String, String> = emptyMap(),
    val explainabilityFactors: List<ForensicExplainabilityFactor> = emptyList(),
    val limitationsNote: String
)
