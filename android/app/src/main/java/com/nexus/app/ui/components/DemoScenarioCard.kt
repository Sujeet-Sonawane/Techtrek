package com.nexus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.ForensicVerdict
import com.nexus.app.data.model.InvestigationReport
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
fun DemoScenarioCard(
    report: InvestigationReport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Media Type & Risk Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NexusSurfaceElevated)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (report.mediaMetadata.isVideo) "VIDEO" else "AUDIO",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlueLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = report.mediaMetadata.formattedDuration,
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted,
                        fontSize = 10.sp
                    )
                }

                // Risk Level Tag
                val (riskColor, riskBg) = when (report.riskLevel) {
                    "CRITICAL RISK" -> Pair(NexusRed, NexusRedDark)
                    "HIGH RISK" -> Pair(NexusRed, NexusRedDark)
                    "MODERATE RISK" -> Pair(NexusAmber, NexusAmberDark)
                    else -> Pair(NexusGreen, NexusGreenDark)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(riskBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = report.riskLevel,
                        style = MaterialTheme.typography.labelSmall,
                        color = riskColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Scenario Title
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Dual Verdict Badges (Forensics vs Truth)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Forensic Pill
                val forensicColor = when (report.forensicAnalysis.verdict) {
                    ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED -> NexusRed
                    ForensicVerdict.NO_SUSPICIOUS_INDICATORS_DETECTED -> NexusGreen
                    else -> NexusAmber
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(NexusSurfaceElevated)
                        .border(0.5.dp, forensicColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(6.dp)
                ) {
                    Column {
                        Text("MEDIA FORENSICS", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 8.sp)
                        Text(
                            text = when (report.forensicAnalysis.verdict) {
                                ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED -> "Suspicious"
                                ForensicVerdict.NO_SUSPICIOUS_INDICATORS_DETECTED -> "Authentic"
                                else -> "Inconclusive"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = forensicColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                // TruthCheck Pill
                val truthColor = when {
                    report.truthCheckResult.verdict.isGreen -> NexusGreen
                    report.truthCheckResult.verdict.isRed -> NexusRed
                    else -> NexusAmber
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(NexusSurfaceElevated)
                        .border(0.5.dp, truthColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(6.dp)
                ) {
                    Column {
                        Text("TRUTHCHECK", style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 8.sp)
                        Text(
                            text = when {
                                report.truthCheckResult.verdict.isGreen -> "Verified Real"
                                report.truthCheckResult.verdict.isRed -> "Debunked False"
                                else -> "Misleading / Unverified"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = truthColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Inspect Case Findings",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlueLight,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = NexusBlueLight,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
