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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.DemoScenarios
import com.nexus.app.ui.components.DemoScenarioCard
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBluePrimary
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusDarkBg
import com.nexus.app.ui.theme.NexusGreen
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusSurfaceElevated
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.viewmodel.NexusScreen
import com.nexus.app.viewmodel.NexusViewModel

@Composable
fun HomeScreen(
    viewModel: NexusViewModel,
    onPickVideo: () -> Unit,
    onPickAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexusDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(NexusBlueLight)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TECHTREK 2026 • AI MEDIA FORENSICS",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Is This Video Real?\nIs Its Claim True?",
                    style = MaterialTheme.typography.headlineLarge,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Investigate suspicious videos, inspect possible manipulation and check important claims against reliable sources.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = NexusTextSecondary
                )
            }
        }

        // Signature Capability Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBluePrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
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
                        tint = NexusBlueLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DUAL VERDICT ARCHITECTURE",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlueLight,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Separate evaluations for Media Forensics (Panel A) and Claim TruthCheck (Panel B). Authentic videos can carry false claims; manipulated recordings can speak facts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary
                        )
                    }
                }
            }
        }

        // 4 Primary Actions Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "PRIMARY ACTIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionCard(
                        title = "Analyze a Video",
                        subtitle = "MP4, WebM, MOV",
                        icon = Icons.Default.PlayArrow,
                        accentColor = NexusBlueLight,
                        onClick = onPickVideo,
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Analyze Audio",
                        subtitle = "WAV, MP3, AAC, M4A",
                        icon = Icons.Default.Info,
                        accentColor = NexusGreen,
                        onClick = onPickAudio,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionCard(
                        title = "Verify Scheme",
                        subtitle = "PIB & myScheme FactCheck",
                        icon = Icons.Default.Search,
                        accentColor = Color(0xFFA855F7),
                        onClick = { viewModel.navigateTo(NexusScreen.TRUTH_CHECK) },
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Compare Two",
                        subtitle = "Side-by-side Forensic Diff",
                        icon = Icons.Default.Refresh,
                        accentColor = Color(0xFFF59E0B),
                        onClick = { viewModel.navigateTo(NexusScreen.COMPARE) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Demonstration Cases Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DEMONSTRATION CASES (OFFLINE READY)",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Pre-loaded real-world scenarios across the dual forensic matrix",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }
            }
        }

        // 8 Interactive Demo Scenarios
        items(DemoScenarios.scenarios) { scenario ->
            DemoScenarioCard(
                report = scenario,
                onClick = { viewModel.loadScenario(scenario.id) }
            )
        }

        // Indian Consumer Safety Footer
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NexusSurface)
                    .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "CITIZEN SAFETY ADVISORY",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Central government welfare schemes never charge an upfront fee. If a video directs you to enter your UPI PIN or pay money, treat it as fraudulent. Dial 1930 for cyber helpline assistance.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 11.sp
            )
        }
    }
}
