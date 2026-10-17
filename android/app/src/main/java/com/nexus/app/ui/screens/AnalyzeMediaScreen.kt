package com.nexus.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.MediaMetadata
import com.nexus.app.ui.theme.NexusBlueGlow
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
fun AnalyzeMediaScreen(
    viewModel: NexusViewModel,
    onPickMedia: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedMedia by viewModel.selectedMedia.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisProgress by viewModel.analysisProgress.collectAsState()
    val analysisStep by viewModel.analysisStep.collectAsState()
    val animatedProgress by animateFloatAsState(targetValue = analysisProgress, label = "progress")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexusDarkBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        Column {
            Text(
                text = "Analyze Recording",
                style = MaterialTheme.typography.headlineMedium,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Inspect acoustic integrity, vocal biomechanics, synchronization, and verify spoken claims against official sources.",
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary
            )
        }

        // Upload / Select Box
        if (selectedMedia == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NexusBorder, RoundedCornerShape(16.dp))
                    .clickable { onPickMedia() },
                colors = CardDefaults.cardColors(containerColor = NexusSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(NexusBluePrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = NexusBlueLight,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Audio or Video Recording",
                        style = MaterialTheme.typography.titleLarge,
                        color = NexusTextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Supported: MP4, WebM, MOV, WAV, MP3, AAC, M4A",
                        style = MaterialTheme.typography.bodySmall,
                        color = NexusTextMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onPickMedia,
                        colors = ButtonDefaults.buttonColors(containerColor = NexusBluePrimary)
                    ) {
                        Text("Browse Device Files", color = NexusTextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Media Loaded Inspector
            MediaInspectorCard(
                media = selectedMedia!!,
                onRePick = onPickMedia
            )

            // Analysis In Progress Card
            if (isAnalyzing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NexusBluePrimary, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = NexusSurfaceElevated),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = NexusBlueLight,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "FORENSIC & FACT INVESTIGATION IN PROGRESS",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlueLight,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = analysisStep,
                            style = MaterialTheme.typography.bodyMedium,
                            color = NexusTextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NexusBlueLight,
                            trackColor = NexusBorder,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = String.format("%d%% Completed", (animatedProgress * 100).toInt()),
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted
                        )
                    }
                }
            } else {
                // CTA to Start Analysis
                Button(
                    onClick = { viewModel.startAnalysis() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NexusBluePrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = NexusTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Run Dual Investigation (Forensics + TruthCheck)",
                        style = MaterialTheme.typography.titleMedium,
                        color = NexusTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Processing Transparency Notice
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NexusSurface)
                .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NexusBlueLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PRIVACY & LOCAL PROCESSING DISCLOSURE",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Acoustic signal processing, F0 fundamental pitch variance, and speech spectrograms are evaluated on-device. When cloud neural verification is configured in Settings, explicit notification will be displayed before transmission.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary
                )
            }
        }
    }
}

@Composable
private fun MediaInspectorCard(
    media: MediaMetadata,
    onRePick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NexusBluePrimary.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (media.isVideo) "VIDEO FILE" else "AUDIO FILE",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onRePick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Change File", style = MaterialTheme.typography.labelSmall, color = NexusTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = media.fileName,
                style = MaterialTheme.typography.titleLarge,
                color = NexusTextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Technical Specifications Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecBox("Duration", media.formattedDuration, modifier = Modifier.weight(1f))
                SpecBox("Format", media.format, modifier = Modifier.weight(1f))
                SpecBox("File Size", media.formattedSize, modifier = Modifier.weight(1f))
            }

            if (media.resolution != null || media.bitRateKbps != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (media.resolution != null) {
                        SpecBox("Resolution", media.resolution, modifier = Modifier.weight(1f))
                    }
                    if (media.bitRateKbps != null) {
                        SpecBox("Bitrate", "${media.bitRateKbps} kbps", modifier = Modifier.weight(1f))
                    }
                    SpecBox("Channels", if (media.channels == 1) "Mono" else "Stereo", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SpecBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .padding(8.dp)
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.bodyMedium, color = NexusTextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}
