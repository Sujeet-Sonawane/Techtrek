package com.nexus.app.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.InvestigationReport
import com.nexus.app.ui.components.TimestampedTimeline
import com.nexus.app.ui.components.VerdictPanelA
import com.nexus.app.ui.components.VerdictPanelB
import com.nexus.app.ui.theme.NexusAmber
import com.nexus.app.ui.theme.NexusAmberDark
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBluePrimary
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusDarkBg
import com.nexus.app.ui.theme.NexusGreen
import com.nexus.app.ui.theme.NexusGreenDark
import com.nexus.app.ui.theme.NexusRed
import com.nexus.app.ui.theme.NexusRedDark
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusSurfaceElevated
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.viewmodel.NexusViewModel

@Composable
fun InvestigationReportScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val reportState by viewModel.activeReport.collectAsState()
    val context = LocalContext.current

    if (reportState == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(NexusDarkBg),
            contentAlignment = Alignment.Center
        ) {
            Text("No Active Investigation Report Selected", color = NexusTextSecondary)
        }
        return
    }

    val report = reportState!!
    val isSaved = viewModel.isReportSaved(report.id)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexusDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation & Action Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = NexusTextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Save / Bookmark Button
                    IconButton(onClick = { viewModel.toggleSaveReport(report) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isSaved) "Saved" else "Save Report",
                            tint = if (isSaved) NexusRed else NexusTextMuted
                        )
                    }

                    // Share Button
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "NEXUS Forensics Report: ${report.title}\n" +
                                        "Risk Level: ${report.riskLevel}\n" +
                                        "Panel A Forensics: ${report.forensicAnalysis.verdict.label}\n" +
                                        "Panel B TruthCheck: ${report.truthCheckResult.verdict.title}\n" +
                                        "Verified via PIB Fact Check & myScheme"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share NEXUS Investigation Report"))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = NexusTextPrimary
                        )
                    }
                }
            }
        }

        // Title and Risk Badge
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val (riskBg, riskBorder, riskText) = when (report.riskLevel) {
                        "CRITICAL RISK" -> Triple(NexusRedDark, NexusRed, "CRITICAL RISK")
                        "HIGH RISK" -> Triple(NexusRedDark, NexusRed, "HIGH RISK")
                        "MODERATE RISK" -> Triple(NexusAmberDark, NexusAmber, "MODERATE RISK")
                        else -> Triple(NexusGreenDark, NexusGreen, "SAFE / VERIFIED")
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(riskBg)
                            .border(1.dp, riskBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = riskText,
                            style = MaterialTheme.typography.labelSmall,
                            color = riskBorder,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = report.formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = report.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${report.mediaMetadata.fileName} • ${report.mediaMetadata.format} • ${report.mediaMetadata.formattedDuration} • ${report.mediaMetadata.formattedSize}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusBlueLight
                )
            }
        }

        // Primary Warning Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (report.isMediaManipulated || report.isClaimFalse) NexusRed else NexusGreen, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurfaceElevated),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = if (report.isMediaManipulated || report.isClaimFalse) NexusRed else NexusGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = report.primaryWarning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NexusTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Panel A: Media Forensics
        item {
            VerdictPanelA(analysis = report.forensicAnalysis)
        }

        // Panel B: TruthCheck Claim Verification
        item {
            VerdictPanelB(result = report.truthCheckResult)
        }

        // Timestamped Findings Timeline
        item {
            TimestampedTimeline(findings = report.forensicAnalysis.timelineFindings)
        }

        // Spoken Transcript Box
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
                        text = "EXTRACTED SPOKEN TRANSCRIPT",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${report.transcript}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NexusTextPrimary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        // Technical Provenance Metadata Box
        if (report.forensicAnalysis.technicalProvenance.isNotEmpty()) {
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
                            text = "TECHNICAL PROVENANCE & CONTAINER METADATA",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        report.forensicAnalysis.technicalProvenance.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = key, style = MaterialTheme.typography.bodySmall, color = NexusTextMuted)
                                Text(text = value, style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
