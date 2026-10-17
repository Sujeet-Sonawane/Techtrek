package com.nexus.app.data.model

data class ForensicExplainabilityFactor(
    val factorName: String,
    val observedValue: String,
    val expectedNormalRange: String,
    val isSuspicious: Boolean,
    val impactWeight: String, // e.g. "+35% Suspicion", "-25% Natural"
    val diagnosticSummary: String,
    val technicalRationale: String
)
