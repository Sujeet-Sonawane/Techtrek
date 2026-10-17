package com.nexus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.nexus.app.data.model.InvestigationReport
import com.nexus.app.ui.theme.NexusAmber
import com.nexus.app.ui.theme.NexusAmberDark
import com.nexus.app.ui.theme.NexusBlueLight
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
fun SavedReportsScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val savedReports by viewModel.savedReports.collectAsState()

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
                    text = "Saved Investigations",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Historical forensic reports, acoustic spectra, and official claim verification records stored on-device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary
                )
            }
        }

        if (savedReports.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved investigation reports yet.\nRun an analysis and tap the bookmark icon to save.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NexusTextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(savedReports) { report ->
                SavedReportItem(
                    report = report,
                    onClick = { viewModel.loadScenario(report.id) },
                    onDelete = { viewModel.toggleSaveReport(report) }
                )
            }
        }
    }
}

@Composable
private fun SavedReportItem(
    report: InvestigationReport,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val (riskColor, riskBg) = when (report.riskLevel) {
        "CRITICAL RISK", "HIGH RISK" -> Pair(NexusRed, NexusRedDark)
        "MODERATE RISK" -> Pair(NexusAmber, NexusAmberDark)
        else -> Pair(NexusGreen, NexusGreenDark)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = report.formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = NexusTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = report.primaryWarning,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${report.mediaMetadata.format} • ${report.mediaMetadata.formattedDuration}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusBlueLight,
                    fontSize = 11.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View Report",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
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
}
