package com.nexus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.ForensicAnalysis
import com.nexus.app.data.model.ForensicVerdict
import com.nexus.app.ui.theme.NexusAmber
import com.nexus.app.ui.theme.NexusAmberDark
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusGreen
import com.nexus.app.ui.theme.NexusGreenDark
import com.nexus.app.ui.theme.NexusRed
import com.nexus.app.ui.theme.NexusRedDark
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusSurfaceElevated
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.ui.theme.NexusTextSecondary

@Composable
fun VerdictPanelA(
    analysis: ForensicAnalysis,
    modifier: Modifier = Modifier
) {
    val badge = when (analysis.verdict) {
        ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED ->
            PanelABadge(NexusRedDark, NexusRed, analysis.verdict.label, Icons.Default.Warning)
        ForensicVerdict.NO_SUSPICIOUS_INDICATORS_DETECTED ->
            PanelABadge(NexusGreenDark, NexusGreen, analysis.verdict.label, Icons.Default.Check)
        ForensicVerdict.INCONCLUSIVE ->
            PanelABadge(NexusAmberDark, NexusAmber, analysis.verdict.label, Icons.Default.Info)
        ForensicVerdict.MODEL_UNAVAILABLE ->
            PanelABadge(NexusSurfaceElevated, NexusBorder, analysis.verdict.label, Icons.Default.Info)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Panel Label & Confidence
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(badge.border)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PANEL A: MEDIA FORENSICS",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = analysis.confidence.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Large Verdict Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(badge.bg.copy(alpha = 0.5f))
                    .border(1.dp, badge.border, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = badge.icon,
                        contentDescription = null,
                        tint = badge.border,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = badge.text,
                        style = MaterialTheme.typography.titleLarge,
                        color = NexusTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Headline & Summary
            Text(
                text = analysis.headline,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = analysis.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Acoustic Metrics Grid
            Text(
                text = "ACOUSTIC SIGNATURE MEASUREMENTS",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricItem(
                    label = "Vocal Jitter",
                    value = String.format("%.2f%%", analysis.audioAuthenticity.jitterPercent),
                    subtext = if (analysis.audioAuthenticity.jitterPercent < 0.2f) "Suspiciously Low" else "Natural Tremor",
                    modifier = Modifier.weight(1f)
                )
                MetricItem(
                    label = "Pitch Variance",
                    value = String.format("%.1f Hz", analysis.audioAuthenticity.pitchVarianceHz),
                    subtext = "F0 Contour",
                    modifier = Modifier.weight(1f)
                )
                MetricItem(
                    label = "Spectral SNR",
                    value = String.format("%.1f dB", analysis.audioIntegrity.snrDb),
                    subtext = "Signal to Noise",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Video Sync (if present)
            if (analysis.videoSync != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexusSurfaceElevated)
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "A/V Synchronization",
                                style = MaterialTheme.typography.labelLarge,
                                color = NexusTextPrimary
                            )
                            Text(
                                text = analysis.videoSync.lipSyncAlignment,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (analysis.videoSync.visualArtifactsDetected) NexusRed else NexusGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = analysis.videoSync.visualNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Forensic Explainability Factor Breakdown
            if (analysis.explainabilityFactors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "FORENSIC DECISION TREE & EXPLAINABILITY",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlueLight,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analysis.explainabilityFactors.forEach { factor ->
                        ExplainabilityCard(factor = factor)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Limitations Note
            Text(
                text = "LIMITATIONS: ${analysis.limitationsNote}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ExplainabilityCard(factor: com.nexus.app.data.model.ForensicExplainabilityFactor) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .border(0.5.dp, if (factor.isSuspicious) NexusRed.copy(alpha = 0.5f) else NexusGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = factor.factorName,
                    style = MaterialTheme.typography.labelLarge,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = factor.impactWeight,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (factor.isSuspicious) NexusRed else NexusGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${factor.observedValue} (Normal: ${factor.expectedNormalRange})",
                style = MaterialTheme.typography.bodySmall,
                color = NexusBlueLight,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = factor.diagnosticSummary,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Why: ${factor.technicalRationale}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun MetricItem(
    label: String,
    value: String,
    subtext: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary,
                fontSize = 9.sp
            )
        }
    }
}

private data class PanelABadge(
    val bg: Color,
    val border: Color,
    val text: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
