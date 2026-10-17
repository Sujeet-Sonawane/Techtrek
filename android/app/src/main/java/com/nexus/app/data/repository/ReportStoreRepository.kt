package com.nexus.app.data.repository

import android.content.Context
import com.nexus.app.data.model.DemoScenarios
import com.nexus.app.data.model.InvestigationReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportStoreRepository(private val context: Context) {

    private val _savedReports = MutableStateFlow<List<InvestigationReport>>(emptyList())
    val savedReports: StateFlow<List<InvestigationReport>> = _savedReports.asStateFlow()

    init {
        // Initialize with default saved demo reports
        val initial = DemoScenarios.scenarios.take(3).map {
            it.copy(isSaved = true)
        }
        _savedReports.value = initial
    }

    fun saveReport(report: InvestigationReport) {
        val current = _savedReports.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.id == report.id }
        if (existingIndex >= 0) {
            current[existingIndex] = report.copy(isSaved = true)
        } else {
            current.add(0, report.copy(isSaved = true))
        }
        _savedReports.value = current
    }

    fun removeReport(reportId: String) {
        _savedReports.value = _savedReports.value.filter { it.id != reportId }
    }

    fun isReportSaved(reportId: String): Boolean {
        return _savedReports.value.any { it.id == reportId }
    }
}
