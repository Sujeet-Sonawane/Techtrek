package com.nexus.app

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import com.nexus.app.ui.screens.CallShieldScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.app.ui.screens.AnalyzeMediaScreen
import com.nexus.app.ui.screens.CompareRecordingsScreen
import com.nexus.app.ui.screens.HomeScreen
import com.nexus.app.ui.screens.InvestigationReportScreen
import com.nexus.app.ui.screens.SavedReportsScreen
import com.nexus.app.ui.screens.SettingsScreen
import com.nexus.app.ui.screens.TruthCheckScreen
import com.nexus.app.ui.theme.NexusBlueLight
import com.nexus.app.ui.theme.NexusBluePrimary
import com.nexus.app.ui.theme.NexusBorder
import com.nexus.app.ui.theme.NexusDarkBg
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextMuted
import com.nexus.app.ui.theme.NexusTextPrimary
import com.nexus.app.viewmodel.NexusScreen
import com.nexus.app.viewmodel.NexusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusApp(
    viewModel: NexusViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val context = LocalContext.current

    // Document Picker Launchers
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var name = "uploaded_video.mp4"
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    name = cursor.getString(nameIndex)
                }
            }
            viewModel.onMediaPicked(uri, name)
        }
    }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var name = "uploaded_audio.wav"
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    name = cursor.getString(nameIndex)
                }
            }
            viewModel.onMediaPicked(uri, name)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(NexusBlueLight)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NEXUS",
                            style = MaterialTheme.typography.titleLarge,
                            color = NexusTextPrimary,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FORENSICS",
                            style = MaterialTheme.typography.labelSmall,
                            color = NexusBlueLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.navigateTo(NexusScreen.SAVED_REPORTS) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Saved Reports",
                            tint = if (currentScreen == NexusScreen.SAVED_REPORTS) NexusBlueLight else NexusTextMuted
                        )
                    }
                    IconButton(onClick = { viewModel.navigateTo(NexusScreen.SETTINGS) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = if (currentScreen == NexusScreen.SETTINGS) NexusBlueLight else NexusTextMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NexusDarkBg,
                    titleContentColor = NexusTextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NexusSurface,
                modifier = Modifier.border(0.5.dp, NexusBorder, androidx.compose.ui.graphics.RectangleShape)
            ) {
                NavigationBarItem(
                    selected = currentScreen == NexusScreen.HOME,
                    onClick = { viewModel.navigateTo(NexusScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NexusBlueLight,
                        selectedTextColor = NexusBlueLight,
                        unselectedIconColor = NexusTextMuted,
                        unselectedTextColor = NexusTextMuted,
                        indicatorColor = NexusSurface
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == NexusScreen.CALL_SHIELD,
                    onClick = { viewModel.navigateTo(NexusScreen.CALL_SHIELD) },
                    icon = { Icon(Icons.Default.Call, contentDescription = "Call Guard") },
                    label = { Text("Call Guard", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NexusBlueLight,
                        selectedTextColor = NexusBlueLight,
                        unselectedIconColor = NexusTextMuted,
                        unselectedTextColor = NexusTextMuted,
                        indicatorColor = NexusSurface
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == NexusScreen.ANALYZE,
                    onClick = { viewModel.navigateTo(NexusScreen.ANALYZE) },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Analyze") },
                    label = { Text("Analyze", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NexusBlueLight,
                        selectedTextColor = NexusBlueLight,
                        unselectedIconColor = NexusTextMuted,
                        unselectedTextColor = NexusTextMuted,
                        indicatorColor = NexusSurface
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == NexusScreen.TRUTH_CHECK,
                    onClick = { viewModel.navigateTo(NexusScreen.TRUTH_CHECK) },
                    icon = { Icon(Icons.Default.Search, contentDescription = "TruthCheck") },
                    label = { Text("TruthCheck", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NexusBlueLight,
                        selectedTextColor = NexusBlueLight,
                        unselectedIconColor = NexusTextMuted,
                        unselectedTextColor = NexusTextMuted,
                        indicatorColor = NexusSurface
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == NexusScreen.SAVED_REPORTS,
                    onClick = { viewModel.navigateTo(NexusScreen.SAVED_REPORTS) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                    label = { Text("Saved", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NexusBlueLight,
                        selectedTextColor = NexusBlueLight,
                        unselectedIconColor = NexusTextMuted,
                        unselectedTextColor = NexusTextMuted,
                        indicatorColor = NexusSurface
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NexusScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onPickVideo = { videoPickerLauncher.launch("video/*") },
                    onPickAudio = { audioPickerLauncher.launch("audio/*") }
                )
                NexusScreen.CALL_SHIELD -> CallShieldScreen(viewModel = viewModel)
                NexusScreen.ANALYZE -> AnalyzeMediaScreen(
                    viewModel = viewModel,
                    onPickMedia = { videoPickerLauncher.launch("*/*") }
                )
                NexusScreen.TRUTH_CHECK -> TruthCheckScreen(viewModel = viewModel)
                NexusScreen.COMPARE -> CompareRecordingsScreen(viewModel = viewModel)
                NexusScreen.REPORT -> InvestigationReportScreen(viewModel = viewModel)
                NexusScreen.SAVED_REPORTS -> SavedReportsScreen(viewModel = viewModel)
                NexusScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
