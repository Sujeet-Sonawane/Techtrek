package com.nexus.app.data.model

enum class TruthVerdict(
    val title: String,
    val summary: String,
    val isRed: Boolean,
    val isGreen: Boolean,
    val isAmber: Boolean
) {
    SUPPORTED_BY_OFFICIAL_SOURCES(
        "Supported by Official Sources",
        "Official government notifications and active schemes confirm the stated details.",
        isRed = false, isGreen = true, isAmber = false
    ),
    CONTRADICTED_BY_OFFICIAL_SOURCES(
        "Contradicted by Official Sources",
        "Official records directly contradict key claims made in the recording.",
        isRed = true, isGreen = false, isAmber = false
    ),
    OFFICIALLY_FACT_CHECKED_FALSE(
        "Officially Fact-Checked as Fake",
        "Government fact-checking units (such as PIB Fact Check) have issued a formal alert debunking this claim.",
        isRed = true, isGreen = false, isAmber = false
    ),
    MISLEADING_OR_MISSING_CONTEXT(
        "Misleading or Missing Context",
        "A genuine government initiative is cited, but eligibility terms, timelines, or application procedures are misrepresented.",
        isRed = false, isGreen = false, isAmber = true
    ),
    UNVERIFIED_NO_CORROBORATION(
        "Unverified — No Official Corroboration Found",
        "No matching announcements exist in official gazettes, press releases, or government databases.",
        isRed = false, isGreen = false, isAmber = true
    ),
    INCONCLUSIVE(
        "Inconclusive Evidence",
        "The claims are too vague or generalized to cross-reference against verifiable government databases.",
        isRed = false, isGreen = false, isAmber = true
    )
}

data class TruthCheckResult(
    val verdict: TruthVerdict,
    val confidence: EvidenceConfidence,
    val headline: String,
    val summary: String,
    val evaluatedClaims: List<SpokenClaim> = emptyList(),
    val officialSourcesChecked: List<OfficialSource> = emptyList(),
    val verifiedSchemeFacts: Map<String, String> = emptyMap(),
    val limitationsNote: String,
    val recommendedActions: List<String> = emptyList()
)
