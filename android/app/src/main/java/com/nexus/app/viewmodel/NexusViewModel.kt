package com.nexus.app.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.app.data.model.ClaimCategory
import com.nexus.app.data.model.ClaimEntities
import com.nexus.app.data.model.DemoScenarios
import com.nexus.app.data.model.EvidenceConfidence
import com.nexus.app.data.model.ForensicVerdict
import com.nexus.app.data.model.InvestigationReport
import com.nexus.app.data.model.MediaMetadata
import com.nexus.app.data.model.SpokenClaim
import com.nexus.app.data.model.TruthCheckResult
import com.nexus.app.data.model.TruthVerdict
import com.nexus.app.data.model.CallShieldState
import com.nexus.app.data.model.CallSimulationPreset
import com.nexus.app.data.repository.CallShieldRepository
import com.nexus.app.data.repository.MediaForensicsRepository
import com.nexus.app.data.repository.ReportStoreRepository
import com.nexus.app.data.repository.TruthCheckRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

enum class NexusScreen(val title: String) {
    HOME("NEXUS"),
    CALL_SHIELD("Call Shield"),
    ANALYZE("Analyze Media"),
    TRUTH_CHECK("TruthCheck"),
    COMPARE("Compare Recordings"),
    REPORT("Investigation Report"),
    SAVED_REPORTS("Saved Reports"),
    SETTINGS("Settings & Privacy")
}

data class CompareState(
    val reportA: InvestigationReport? = null,
    val reportB: InvestigationReport? = null,
    val comparisonSummary: String = ""
)

data class NexusSettings(
    val remoteApiUrl: String = "http://10.0.2.2:8000",
    val useRemoteModelIfAvailable: Boolean = false,
    val preferOfficialSourcesOnly: Boolean = true,
    val highSensitivityForensics: Boolean = true,
    val telemetryEnabled: Boolean = false
)

class NexusViewModel(application: Application) : AndroidViewModel(application) {

    private val forensicsRepo = MediaForensicsRepository(application)
    private val truthCheckRepo = TruthCheckRepository()
    private val reportStoreRepo = ReportStoreRepository(application)
    private val callShieldRepo = CallShieldRepository.getInstance(application)

    // Call Shield State
    val callShieldState: StateFlow<CallShieldState> = callShieldRepo.callState
    val isLiveMicListening: StateFlow<Boolean> = callShieldRepo.isLiveMicListening

    // Screen Navigation
    private val _currentScreen = MutableStateFlow(NexusScreen.HOME)
    val currentScreen: StateFlow<NexusScreen> = _currentScreen.asStateFlow()

    // Screen Backstack
    private val screenStack = mutableListOf<NexusScreen>()

    // Current Report for Investigation View
    private val _activeReport = MutableStateFlow<InvestigationReport?>(DemoScenarios.scenarios[0])
    val activeReport: StateFlow<InvestigationReport?> = _activeReport.asStateFlow()

    // Media Analysis Flow State
    private val _selectedMedia = MutableStateFlow<MediaMetadata?>(null)
    val selectedMedia: StateFlow<MediaMetadata?> = _selectedMedia.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisProgress = MutableStateFlow(0f)
    val analysisProgress: StateFlow<Float> = _analysisProgress.asStateFlow()

    private val _analysisStep = MutableStateFlow("Ready")
    val analysisStep: StateFlow<String> = _analysisStep.asStateFlow()

    // TruthCheck Direct Search State
    private val _truthCheckInput = MutableStateFlow("")
    val truthCheckInput: StateFlow<String> = _truthCheckInput.asStateFlow()

    private val _isVerifyingClaim = MutableStateFlow(false)
    val isVerifyingClaim: StateFlow<Boolean> = _isVerifyingClaim.asStateFlow()

    private val _directTruthResult = MutableStateFlow<TruthCheckResult?>(null)
    val directTruthResult: StateFlow<TruthCheckResult?> = _directTruthResult.asStateFlow()

    // Compare Screen State
    private val _compareState = MutableStateFlow(
        CompareState(
            reportA = DemoScenarios.scenarios[0],
            reportB = DemoScenarios.scenarios[1],
            comparisonSummary = "Comparison between AI-generated Deepfake video vs Authentic Press Release"
        )
    )
    val compareState: StateFlow<CompareState> = _compareState.asStateFlow()

    // Saved Reports
    val savedReports: StateFlow<List<InvestigationReport>> = reportStoreRepo.savedReports

    // Settings
    private val _settings = MutableStateFlow(NexusSettings())
    val settings: StateFlow<NexusSettings> = _settings.asStateFlow()

    fun navigateTo(screen: NexusScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            true
        } else {
            false
        }
    }

    fun onMediaPicked(uri: Uri, displayName: String?) {
        viewModelScope.launch {
            val meta = forensicsRepo.extractMetadata(uri, displayName)
            _selectedMedia.value = meta
            navigateTo(NexusScreen.ANALYZE)
        }
    }

    fun startAnalysis() {
        val meta = _selectedMedia.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisProgress.value = 0.15f
            _analysisStep.value = "Extracting acoustic features and speech spectrogram..."
            delay(400)

            _analysisProgress.value = 0.40f
            _analysisStep.value = "Inspecting fundamental pitch variance and vocal jitter..."
            delay(500)

            _analysisProgress.value = 0.65f
            _analysisStep.value = "Detecting sudden phase transitions and lip sync alignment..."
            delay(400)

            _analysisProgress.value = 0.85f
            _analysisStep.value = "Cross-referencing spoken claims against PIB & myScheme..."
            delay(400)

            val forensic = forensicsRepo.analyzeMedia(
                metadata = meta,
                highSensitivity = _settings.value.highSensitivityForensics
            )
            val truth = truthCheckRepo.verifyClaims(meta.fileName)

            val report = InvestigationReport(
                id = UUID.randomUUID().toString(),
                title = "Investigation: ${meta.fileName}",
                mediaMetadata = meta,
                forensicAnalysis = forensic,
                truthCheckResult = truth,
                transcript = "Spoken claims extracted from uploaded recording for forensic analysis and fact verification.",
                riskLevel = if (forensic.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED || truth.verdict == TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE) "CRITICAL RISK" else "MODERATE RISK",
                primaryWarning = if (forensic.verdict == ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED) "Suspicious acoustic signals detected in media." else "Check official portals before acting.",
                actionSteps = truth.recommendedActions
            )

            _analysisProgress.value = 1.0f
            _analysisStep.value = "Analysis Complete"
            _activeReport.value = report
            _isAnalyzing.value = false
            navigateTo(NexusScreen.REPORT)
        }
    }

    fun loadScenario(scenarioId: String) {
        val scenario = DemoScenarios.getScenarioById(scenarioId)
        if (scenario != null) {
            _activeReport.value = scenario
            navigateTo(NexusScreen.REPORT)
        }
    }

    fun updateTruthCheckInput(query: String) {
        _truthCheckInput.value = query
    }

    fun performTruthCheck(query: String? = null) {
        val text = query ?: _truthCheckInput.value
        if (text.isBlank()) return

        viewModelScope.launch {
            _isVerifyingClaim.value = true
            delay(600)
            val result = truthCheckRepo.verifyClaims(text)
            _directTruthResult.value = result
            _isVerifyingClaim.value = false
        }
    }

    fun toggleSaveReport(report: InvestigationReport) {
        if (reportStoreRepo.isReportSaved(report.id)) {
            reportStoreRepo.removeReport(report.id)
            if (_activeReport.value?.id == report.id) {
                _activeReport.value = _activeReport.value?.copy(isSaved = false)
            }
        } else {
            reportStoreRepo.saveReport(report)
            if (_activeReport.value?.id == report.id) {
                _activeReport.value = _activeReport.value?.copy(isSaved = true)
            }
        }
    }

    fun isReportSaved(reportId: String): Boolean {
        return reportStoreRepo.isReportSaved(reportId)
    }

    fun updateSettings(newSettings: NexusSettings) {
        _settings.value = newSettings
    }

    fun setCompareReports(reportA: InvestigationReport, reportB: InvestigationReport) {
        _compareState.value = CompareState(
            reportA = reportA,
            reportB = reportB,
            comparisonSummary = "Side-by-side forensic and factual cross-examination"
        )
        navigateTo(NexusScreen.COMPARE)
    }

    fun startCallSimulation(preset: CallSimulationPreset) {
        callShieldRepo.startSimulation(preset, viewModelScope)
        navigateTo(NexusScreen.CALL_SHIELD)
    }

    fun startLiveMicListener() {
        callShieldRepo.startLiveMicListener(viewModelScope)
        navigateTo(NexusScreen.CALL_SHIELD)
    }

    fun triggerTestIncomingCall(callerNumber: String = "+91 98201 44821") {
        com.nexus.app.service.CallShieldService.startCallGuard(getApplication(), callerNumber, "RINGING")
        navigateTo(NexusScreen.CALL_SHIELD)
    }

    fun stopCallShield() {
        com.nexus.app.service.CallShieldService.stopCallGuard(getApplication())
        callShieldRepo.stopCallShield()
    }
}
