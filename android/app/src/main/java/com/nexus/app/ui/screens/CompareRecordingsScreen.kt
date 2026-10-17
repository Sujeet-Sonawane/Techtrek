package com.nexus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.DemoScenarios
import com.nexus.app.data.model.ForensicVerdict
import com.nexus.app.data.model.InvestigationReport
import com.nexus.app.ui.theme.NexusAmber
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBluePrimary
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusDarkBg
import com.nexus.app.ui.theme.NexusGreen
import com.nexus.app.ui.theme.NexusRed
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusSurfaceElevated
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.viewmodel.NexusViewModel

@Composable
fun CompareRecordingsScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val compareState by viewModel.compareState.collectAsState()
    val reportA = compareState.reportA ?: DemoScenarios.scenarios[0]
    val reportB = compareState.reportB ?: DemoScenarios.scenarios[1]

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexusDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = "Compare Two Recordings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Perform side-by-side acoustic diffing, speech spectrogram comparison, and claim divergence analysis between suspected media and authentic reference.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary
                )
            }
        }

        // Comparative Summary Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBluePrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurfaceElevated),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "SIDE-BY-SIDE FORENSIC DIVERGENCE",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recording A displays synthetic pitch flattening and lip-sync asynchrony, while Recording B adheres to natural human biomechanical vibration and official PIB archives.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NexusTextSecondary
                    )
                }
            }
        }

        // Side-by-side Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CompareColumnItem(
                    label = "RECORDING A (SUSPECTED)",
                    report = reportA,
                    modifier = Modifier.weight(1f)
                )
                CompareColumnItem(
                    label = "RECORDING B (AUTHENTIC)",
                    report = reportB,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Metric Comparison Matrix
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ACOUSTIC & SYNCHRONIZATION DIFFERENTIAL MATRIX",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ComparisonRow(
                        metric = "Forensic Verdict",
                        valA = if (reportA.forensicAnalysis.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED) "Suspicious" else "Authentic",
                        valB = if (reportB.forensicAnalysis.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED) "Suspicious" else "Authentic",
                        colorA = if (reportA.forensicAnalysis.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED) NexusRed else NexusGreen,
                        colorB = if (reportB.forensicAnalysis.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED) NexusRed else NexusGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ComparisonRow(
                        metric = "Vocal Jitter (%)",
                        valA = String.format("%.2f%% (Artificial)", reportA.forensicAnalysis.audioAuthenticity.jitterPercent),
                        valB = String.format("%.2f%% (Organic)", reportB.forensicAnalysis.audioAuthenticity.jitterPercent),
                        colorA = NexusRed,
                        colorB = NexusGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ComparisonRow(
                        metric = "F0 Pitch Variance",
                        valA = String.format("%.1f Hz (Flattened)", reportA.forensicAnalysis.audioAuthenticity.pitchVarianceHz),
                        valB = String.format("%.1f Hz (Dynamic)", reportB.forensicAnalysis.audioAuthenticity.pitchVarianceHz),
                        colorA = NexusRed,
                        colorB = NexusGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ComparisonRow(
                        metric = "A/V Lip-Sync Drift",
                        valA = if (reportA.forensicAnalysis.videoSync != null) "${reportA.forensicAnalysis.videoSync.avOffsetMs} ms (Lag)" else "N/A (Audio)",
                        valB = if (reportB.forensicAnalysis.videoSync != null) "${reportB.forensicAnalysis.videoSync.avOffsetMs} ms (Sync)" else "N/A (Audio)",
                        colorA = NexusRed,
                        colorB = NexusGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ComparisonRow(
                        metric = "Official Truth Status",
                        valA = "Debunked False",
                        valB = "Supported by PIB",
                        colorA = NexusRed,
                        colorB = NexusGreen
                    )
                }
            }
        }

        // Swap / Alternate Scenarios Button
        item {
            Button(
                onClick = {
                    viewModel.setCompareReports(
                        DemoScenarios.scenarios[2], // Recycled speech
                        DemoScenarios.scenarios[3]  // Fake APK
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NexusSurfaceElevated)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = NexusBlueLight)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Compare Another Pair (Recycled Speech vs Malicious APK)", color = NexusTextPrimary)
            }
        }
    }
}

@Composable
private fun CompareColumnItem(
    label: String,
    report: InvestigationReport,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, NexusBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = NexusSurfaceElevated),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = NexusBlueLight,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${report.mediaMetadata.format} • ${report.mediaMetadata.formattedDuration}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    metric: String,
    valA: String,
    valB: String,
    colorA: androidx.compose.ui.graphics.Color,
    colorB: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = metric,
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = valA,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorA,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = valB,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorB,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
