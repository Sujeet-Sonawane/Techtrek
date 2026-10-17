package com.nexus.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.data.model.CallSecurityStatus
import com.nexus.app.data.model.CallShieldState
import com.nexus.app.data.model.CallSimulationPreset
import com.nexus.app.data.model.CallSimulationPresets
import com.nexus.app.data.model.ForensicExplainabilityFactor
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
fun CallShieldScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val callState by viewModel.callShieldState.collectAsState()
    val isLiveMicListening by viewModel.isLiveMicListening.collectAsState()
    var autoScreenCallsEnabled by remember { mutableStateOf(true) }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(NexusBlueLight)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE CALL SHIELD • TRUECALLER-STYLE VERIFICATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusBlueLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "AI Call Guard & Voice Authenticity",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Real-time acoustic laryngeal tremor screening for incoming phone calls. Instantly distinguishes AI-cloned voices from genuine callers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary
                )
            }
        }

        // Active Call / Live Screen HUD (Truecaller-style card)
        item {
            LiveCallHUDCard(
                callState = callState,
                onDismiss = { viewModel.stopCallShield() }
            )
        }

        // 1-Click Interactive Call Simulation Presets
        item {
            Column {
                Text(
                    text = "INSTANT CALL SIMULATION STATION",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap any scenario below to trigger a live incoming call with instant AI voice verification:",
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CallSimulationPresets.presets.forEach { preset ->
                        PresetCallCard(
                            preset = preset,
                            onClick = { viewModel.startCallSimulation(preset) }
                        )
                    }
                }
            }
        }

        // Live Microphone Call Listener Mode
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (isLiveMicListening) NexusBlueLight else NexusBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = NexusSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Real-Time Microphone Guard",
                                style = MaterialTheme.typography.titleMedium,
                                color = NexusTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Listen to any voice or phone call in the room via phone microphone.",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary
                            )
                        }

                        Button(
                            onClick = {
                                if (isLiveMicListening) {
                                    viewModel.stopCallShield()
                                } else {
                                    viewModel.startLiveMicListener()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLiveMicListening) NexusRedDark else NexusBluePrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isLiveMicListening) "Stop Mic" else "Start Live Mic",
                                color = NexusTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Automatic Telephony Screen Setting
        item {
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Auto-Screen Cellular Calls",
                                style = MaterialTheme.typography.titleMedium,
                                color = NexusTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Display Truecaller-style security HUD when incoming calls arrive.",
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary
                            )
                        }
                        Switch(
                            checked = autoScreenCallsEnabled,
                            onCheckedChange = { autoScreenCallsEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexusBluePrimary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveCallHUDCard(
    callState: CallShieldState,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val (badgeBg, badgeBorder, badgeIcon) = when {
        callState.status.isRed -> Triple(NexusRedDark, NexusRed, Icons.Default.Warning)
        callState.status.isGreen -> Triple(NexusGreenDark, NexusGreen, Icons.Default.Check)
        else -> Triple(NexusAmberDark, NexusAmber, Icons.Default.Info)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, if (callState.isActiveCall) badgeBorder else NexusBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Caller ID Bar (Truecaller Style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (callState.isActiveCall) badgeBorder.copy(alpha = 0.2f) else NexusSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = if (callState.isActiveCall) badgeBorder else NexusBlueLight,
                            modifier = Modifier
                                .size(24.dp)
                                .then(if (callState.isRinging) Modifier.scale(pulseScale) else Modifier)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = callState.callerName,
                            style = MaterialTheme.typography.titleLarge,
                            color = NexusTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${callState.callerNumber} • ${callState.callerLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                if (callState.isActiveCall) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Reset", style = MaterialTheme.typography.labelSmall, color = NexusTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Large Verdict Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeBg.copy(alpha = 0.5f))
                    .border(1.dp, badgeBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = badgeBorder,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = callState.alertHeadline,
                            style = MaterialTheme.typography.titleMedium,
                            color = NexusTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = callState.status.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Real-Time Acoustic Measurement Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LiveMetricPill(
                    label = "Vocal Jitter",
                    value = String.format("%.2f%%", callState.liveJitterPercent),
                    evaluation = if (callState.liveJitterPercent < 0.20f) "Artificial (<0.2%)" else "Natural Human",
                    color = if (callState.liveJitterPercent < 0.20f) NexusRed else NexusGreen,
                    modifier = Modifier.weight(1f)
                )
                LiveMetricPill(
                    label = "F0 Pitch",
                    value = String.format("%.0f Hz", callState.livePitchHz),
                    evaluation = "Voice Dynamic",
                    color = NexusBlueLight,
                    modifier = Modifier.weight(1f)
                )
                LiveMetricPill(
                    label = "AI Probability",
                    value = String.format("%.0f%%", callState.syntheticLikelihood * 100),
                    evaluation = if (callState.syntheticLikelihood > 0.5f) "High Risk" else "Low Risk",
                    color = if (callState.syntheticLikelihood > 0.5f) NexusRed else NexusGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            // High Risk Keywords Detected
            if (callState.detectedKeywords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "FINANCIAL THREAT KEYWORDS DETECTED",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusRed,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    callState.detectedKeywords.forEach { kw ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NexusRedDark)
                                .border(0.5.dp, NexusRed, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = kw, style = MaterialTheme.typography.bodySmall, color = NexusRed, fontSize = 10.sp)
                        }
                    }
                }
            }

            // Transcript Box
            if (callState.liveTranscript.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexusSurfaceElevated)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "LIVE SPEECH TRANSCRIPT",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusTextMuted,
                            fontSize = 9.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"${callState.liveTranscript}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = NexusTextPrimary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            // Explainability Breakdown
            if (callState.explainabilityFactors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "FORENSIC DECISION TREE & EXPLAINABILITY",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusBlueLight,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                callState.explainabilityFactors.forEach { factor ->
                    CallExplainabilityItem(factor = factor)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Safety Guidance
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "SAFETY GUIDANCE: ${callState.safetyAdvice}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun LiveMetricPill(
    label: String,
    value: String,
    evaluation: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NexusSurfaceElevated)
            .padding(8.dp)
    ) {
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = NexusTextMuted, fontSize = 9.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(evaluation, style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary, fontSize = 9.sp)
        }
    }
}

@Composable
private fun CallExplainabilityItem(factor: ForensicExplainabilityFactor) {
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
                text = "${factor.observedValue} (Normal range: ${factor.expectedNormalRange})",
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
        }
    }
}

@Composable
private fun PresetCallCard(
    preset: CallSimulationPreset,
    onClick: () -> Unit
) {
    val (statusColor, statusBg) = when {
        preset.targetStatus.isRed -> Pair(NexusRed, NexusRedDark)
        preset.targetStatus.isGreen -> Pair(NexusGreen, NexusGreenDark)
        else -> Pair(NexusAmber, NexusAmberDark)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NexusBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = NexusSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(statusBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = preset.callerType,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
                Text(
                    text = preset.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = preset.callerName,
                    style = MaterialTheme.typography.bodySmall,
                    color = NexusTextMuted,
                    fontSize = 11.sp
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = NexusBlueLight,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
