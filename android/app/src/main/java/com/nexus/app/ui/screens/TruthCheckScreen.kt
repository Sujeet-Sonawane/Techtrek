package com.nexus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.nexus.app.ui.components.VerdictPanelB
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBluePrimary
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusDarkBg
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusSurfaceElevated
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.viewmodel.NexusViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TruthCheckScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val inputQuery by viewModel.truthCheckInput.collectAsState()
    val isVerifying by viewModel.isVerifyingClaim.collectAsState()
    val directResult by viewModel.directTruthResult.collectAsState()

    val quickClaims = listOf(
        "Diwali Dhan Labh ₹50,000 Scheme",
        "Digital Arrest threat from CBI officer",
        "PM-KISAN 17th installment release ₹2,000",
        "Ayushman Bharat ₹5 Lakh health insurance",
        "PM Kanya Vivah Yojana ₹1,00,000 cash grant"
    )

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
                    text = "TruthCheck™ Claim Verification",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NexusTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cross-examine spoken claims, scheme promises, benefit amounts, and application portals against official Indian government databases.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NexusTextSecondary
                )
            }
        }

        // Query Input Field
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
                        text = "ENTER CLAIM OR SCHEME NAME",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputQuery,
                        onValueChange = { viewModel.updateTruthCheckInput(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("e.g. PM Diwali Dhan Labh ₹50,000 online registration", color = NexusTextMuted, fontSize = 13.sp)
                        },
                        trailingIcon = {
                            if (inputQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateTruthCheckInput("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = NexusTextMuted
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusBluePrimary,
                            unfocusedBorderColor = NexusBorder,
                            focusedTextColor = NexusTextPrimary,
                            unfocusedTextColor = NexusTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = false,
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.performTruthCheck() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NexusBluePrimary),
                        enabled = inputQuery.isNotBlank() && !isVerifying
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                color = NexusTextPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Querying PIB & myScheme...", color = NexusTextPrimary)
                        } else {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NexusTextPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verify Against Official Sources", fontWeight = FontWeight.Bold, color = NexusTextPrimary)
                        }
                    }
                }
            }
        }

        // Quick Search Chips
        item {
            Column {
                Text(
                    text = "COMMON VIRAL CLAIMS",
                    style = MaterialTheme.typography.labelSmall,
                    color = NexusTextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickClaims.forEach { claim ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexusSurfaceElevated)
                                .border(0.5.dp, NexusBorder, RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.updateTruthCheckInput(claim)
                                    viewModel.performTruthCheck(claim)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = claim,
                                style = MaterialTheme.typography.bodySmall,
                                color = NexusTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Direct Verification Result (Panel B)
        if (directResult != null) {
            item {
                VerdictPanelB(result = directResult!!)
            }
        }

        // Official Databases Reference Card
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
                        text = "OFFICIAL GOVERNMENT REGISTRIES",
                        style = MaterialTheme.typography.labelSmall,
                        color = NexusTextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    PortalItem("PIB Fact Check Unit", "factcheck.pib.gov.in", "Official counter to viral misinformation and fake schemes.")
                    Spacer(modifier = Modifier.height(8.dp))
                    PortalItem("myScheme Portal", "myscheme.gov.in", "Official repository of 1,400+ Central and State welfare initiatives.")
                    Spacer(modifier = Modifier.height(8.dp))
                    PortalItem("National Cybercrime Portal", "cybercrime.gov.in • Helpline 1930", "Reporting portal for online fraud and illegal threats.")
                    Spacer(modifier = Modifier.height(8.dp))
                    PortalItem("RBI Sachet Portal", "sachet.rbi.org.in", "Registry of authorized and blacklisted lending entities.")
                }
            }
        }
    }
}

@Composable
private fun PortalItem(title: String, url: String, desc: String) {
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
                Text(text = title, style = MaterialTheme.typography.labelLarge, color = NexusTextPrimary, fontWeight = FontWeight.Bold)
                Text(text = url, style = MaterialTheme.typography.bodySmall, color = NexusBlueLight, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = NexusTextSecondary, fontSize = 11.sp)
        }
    }
}
