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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
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
import com.nexus.app.data.model.MatchVerdict
import com.nexus.app.data.model.OfficialSource
import com.nexus.app.data.model.SpokenClaim
import com.nexus.app.data.model.TruthCheckResult
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
fun VerdictPanelB(
    result: TruthCheckResult,
    modifier: Modifier = Modifier
) {
    val badge = when {
        result.verdict.isGreen ->
            PanelBBadge(NexusGreenDark, NexusGreen, result.verdict.title, Icons.Default.Check)
        result.verdict.isRed ->
            PanelBBadge(NexusRedDark, NexusRed, result.verdict.title, Icons.Default.Close)
        else ->
            PanelBBadge(NexusAmberDark, NexusAmber, result.verdict.title, Icons.Default.Warning)
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
                        text = "PANEL B: CLAIM VERIFICATION (TRUTHCHECK)",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = result.confidence.label,
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
                text = result.headline,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = result.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary
            )

            // Evaluated Claims Cards
            if (result.evaluatedClaims.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "EXTRACTED CLAIMS & ENTITIES",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                result.evaluatedClaims.forEach { claim ->
                    ClaimCard(claim = claim)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Official Sources Checked
            if (result.officialSourcesChecked.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "OFFICIAL CORROBORATION SOURCES",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                result.officialSourcesChecked.forEach { source ->
                    SourceCard(source = source)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Verified Scheme Facts Table
            if (result.verifiedSchemeFacts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "OFFICIAL SCHEME FACTS REGISTRY",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexusSurfaceElevated)
                        .padding(10.dp)
                ) {
                    Column {
                        result.verifiedSchemeFacts.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NexusTextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NexusTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Recommended Next Steps
            if (result.recommendedActions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "RECOMMENDED ACTION FOR CITIZENS",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                result.recommendedActions.forEach { action ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "•", color = NexusBlueLight, modifier = Modifier.padding(end = 6.dp))
                        Text(
                            text = action,
                            style = MaterialTheme.typography.bodyMedium,
                            color = NexusTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Limitations Note
            Text(
                text = "LIMITATIONS: ${result.limitationsNote}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ClaimCard(claim: SpokenClaim) {
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
                    text = claim.category.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlueLight,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = claim.timestampRange,
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\"${claim.claimText}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextPrimary
            )

            // Extracted Entities
            val entities = claim.entities
            if (entities.schemeName != null || entities.claimedAmount != null || entities.feeOrPayment != null || entities.targetUrl != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    if (entities.schemeName != null) {
                        Text("• Scheme: ${entities.schemeName}", style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary)
                    }
                    if (entities.claimedAmount != null) {
                        Text("• Claimed Benefit: ${entities.claimedAmount}", style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary)
                    }
                    if (entities.feeOrPayment != null) {
                        Text("• Fee Demanded: ${entities.feeOrPayment}", style = MaterialTheme.typography.bodySmall, color = NexusRed)
                    }
                    if (entities.targetUrl != null) {
                        Text("• Directed URL: ${entities.targetUrl}", style = MaterialTheme.typography.bodySmall, color = NexusAmber)
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceCard(source: OfficialSource) {
    val (statusColor, statusBg) = when (source.matchVerdict) {
        MatchVerdict.CORROBORATED -> Pair(NexusGreen, NexusGreenDark)
        MatchVerdict.CONTRADICTED, MatchVerdict.OFFICIALLY_DEBUNKED -> Pair(NexusRed, NexusRedDark)
        MatchVerdict.UNVERIFIED_NOT_FOUND -> Pair(NexusAmber, NexusAmberDark)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .border(0.5.dp, NexusBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = source.authorityName,
                    style = MaterialTheme.typography.labelLarge,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = source.matchVerdict.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontSize = 9.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = source.officialUrl,
                style = MaterialTheme.typography.bodySmall,
                color = NexusBlueLight,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = source.quoteOrFact,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary
            )
        }
    }
}

private data class PanelBBadge(
    val bg: Color,
    val border: Color,
    val text: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
