package com.nexus.app.data.model

data class MediaMetadata(
    val fileName: String,
    val uriString: String? = null,
    val format: String, // MP4, WEBM, MOV, WAV, MP3, AAC, M4A
    val isVideo: Boolean,
    val durationSeconds: Double,
    val fileSizeBytes: Long,
    val resolution: String? = null,
    val sampleRateHz: Int? = null,
    val channels: Int? = null,
    val bitRateKbps: Int? = null,
    val codec: String? = null
) {
    val formattedDuration: String
        get() {
            val totalSec = durationSeconds.toInt()
            val minutes = totalSec / 60
            val seconds = totalSec % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    val formattedSize: String
        get() {
            return when {
                fileSizeBytes >= 1024 * 1024 -> String.format("%.1f MB", fileSizeBytes / (1024.0 * 1024.0))
                fileSizeBytes >= 1024 -> String.format("%.1f KB", fileSizeBytes / 1024.0)
                else -> "$fileSizeBytes bytes"
            }
        }
}
