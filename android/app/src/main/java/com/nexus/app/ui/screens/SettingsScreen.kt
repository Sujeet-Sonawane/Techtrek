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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.nexus.app.viewmodel.NexusViewModel

@Composable
fun SettingsScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    var apiUrl by remember(settings.remoteApiUrl) { mutableStateOf(settings.remoteApiUrl) }

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
                    text = "Settings & Privacy",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Configure forensic neural inference endpoints, claim search providers, and review privacy protections.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary
                )
            }
        }

        // Forensics Engine API Configuration
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = NexusBlueLight, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DEEPFAKE DETECTION MODEL ENDPOINT",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = apiUrl,
                        onValueChange = {
                            apiUrl = it
                            viewModel.updateSettings(settings.copy(remoteApiUrl = it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("FastAPI Server URL", color = NexusTextMuted) },
                        placeholder = { Text("http://10.0.2.2:8000", color = NexusTextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusBluePrimary,
                            unfocusedBorderColor = NexusBorder,
                            focusedTextColor = NexusTextPrimary,
                            unfocusedTextColor = NexusTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Use Remote Neural Service", style = MaterialTheme.typography.titleMedium, color = NexusTextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Call remote FastAPI backend when reachable; fallback to on-device heuristics otherwise.", style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary)
                        }
                        Switch(
                            checked = settings.useRemoteModelIfAvailable,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(useRemoteModelIfAvailable = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexusBluePrimary)
                        )
                    }
                }
            }
        }

        // TruthCheck Search Providers
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
                        text = "CLAIM VERIFICATION SEARCH PROVIDER",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Prioritize Official .gov.in Sources", style = MaterialTheme.typography.titleMedium, color = NexusTextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Strictly corroborate against PIB Fact Check, myScheme, and gazette notifications.", style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary)
                        }
                        Switch(
                            checked = settings.preferOfficialSourcesOnly,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(preferOfficialSourcesOnly = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexusBluePrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("High Sensitivity Acoustic Rules", style = MaterialTheme.typography.titleMedium, color = NexusTextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Flag micro-acoustic phase shifts and jitter drops below 0.1%.", style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary)
                        }
                        Switch(
                            checked = settings.highSensitivityForensics,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(highSensitivityForensics = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexusBluePrimary)
                        )
                    }
                }
            }
        }

        // Privacy Disclosures Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurfaceElevated),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NexusGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PRIVACY & SECURITY GUARANTEES",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusGreen,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• On-Device First: Forensic audio spectrum analysis is conducted locally within your phone's memory.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Zero Cloud Retention: Audio and video files are never permanently saved on remote servers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• No Biometric Profiling: Voices are mathematically examined for synthetic markers without creating biometric profiles of callers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextSecondary
                    )
                }
            }
        }

        // App & Hackathon Info
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
                        text = "NEXUS • TECHTREK 2026",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tagline: Don't Trust the Voice. Verify the Claim.\nArchitecture: 100% Native Jetpack Compose + Material 3\nBuilt by Team NEXUS",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted
                    )
                }
            }
        }
    }
}
