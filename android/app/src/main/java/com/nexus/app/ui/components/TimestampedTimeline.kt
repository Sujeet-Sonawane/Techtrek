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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.TimestampedFinding
import com.nexus.app.ui.theme.NexusAmber
import com.nexus.app.ui.theme.NexusAmberDark
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusRed
import com.nexus.app.ui.theme.NexusRedDark
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusSurfaceElevated
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.ui.theme.NexusTextSecondary

@Composable
fun TimestampedTimeline(
    findings: List<TimestampedFinding>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "TIMESTAMPED FORENSIC TIMELINE",
                style = MaterialTheme.typography.labelSmall,
                color = NexusTextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (findings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexusSurfaceElevated)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "No anomalous transitions, phase shifts, or acoustic glitches detected across the recording timeline.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NexusTextSecondary
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    findings.forEach { finding ->
                        TimelineItem(finding = finding)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(finding: TimestampedFinding) {
    val (badgeBg, badgeBorder, badgeText) = when (finding.severity.lowercase()) {
        "high" -> Triple(NexusRedDark, NexusRed, "High Severity")
        "moderate" -> Triple(NexusAmberDark, NexusAmber, "Moderate")
        else -> Triple(NexusSurfaceElevated, NexusBorder, "Info")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .border(0.5.dp, NexusBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            // Timestamp badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NexusBorder)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = finding.formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlueLight,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = finding.findingType,
                        style = MaterialTheme.typography.titleMedium,
                        color = NexusTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeBorder,
                            fontSize = 9.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = finding.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Possible cause: ${finding.possibleCauses}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
