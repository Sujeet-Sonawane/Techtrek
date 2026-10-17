package com.nexus.app.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class InvestigationReport(
    val id: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mediaMetadata: MediaMetadata,
    val forensicAnalysis: ForensicAnalysis,
    val truthCheckResult: TruthCheckResult,
    val transcript: String,
    val riskLevel: String, // "CRITICAL RISK", "HIGH RISK", "MODERATE RISK", "SAFE / VERIFIED"
    val primaryWarning: String,
    val actionSteps: List<String>,
    var isSaved: Boolean = false
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

    val isMediaManipulated: Boolean
        get() = forensicAnalysis.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED

    val isClaimFalse: Boolean
        get() = truthCheckResult.verdict == TruthVerdict.CONTRADICTED_BY_OFFICIAL_SOURCES ||
                truthCheckResult.verdict == TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE
}
