package com.nexus.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nexus.app.MainActivity
import com.nexus.app.R
import com.nexus.app.data.model.CallSecurityStatus
import com.nexus.app.data.repository.CallShieldRepository

class CallShieldService : Service() {

    companion object {
        const val CHANNEL_ID = "nexus_call_shield_channel"
        const val NOTIFICATION_ID = 9001

        const val ACTION_START_CALL_GUARD = "com.nexus.app.action.START_CALL_GUARD"
        const val ACTION_STOP_CALL_GUARD = "com.nexus.app.action.STOP_CALL_GUARD"
        const val EXTRA_CALLER_NUMBER = "extra_caller_number"
        const val EXTRA_CALL_STATE = "extra_call_state"

        fun startCallGuard(context: Context, callerNumber: String, callState: String) {
            val intent = Intent(context, CallShieldService::class.java).apply {
                action = ACTION_START_CALL_GUARD
                putExtra(EXTRA_CALLER_NUMBER, callerNumber)
                putExtra(EXTRA_CALL_STATE, callState)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopCallGuard(context: Context) {
            val intent = Intent(context, CallShieldService::class.java).apply {
                action = ACTION_STOP_CALL_GUARD
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_CALL_GUARD -> {
                CallShieldRepository.getInstance(applicationContext).onCallEnded()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            ACTION_START_CALL_GUARD, null -> {
                val callerNumber = intent?.getStringExtra(EXTRA_CALLER_NUMBER) ?: "Incoming Call"
                val callState = intent?.getStringExtra(EXTRA_CALL_STATE) ?: "RINGING"

                // Update singleton repository state so the UI and overlay immediately react
                val repository = CallShieldRepository.getInstance(applicationContext)
                repository.onIncomingCallReceived(callerNumber, callState == "RINGING")

                val notification = buildCallShieldNotification(callerNumber, callState)
                startForeground(NOTIFICATION_ID, notification)
            }
        }
        return START_NOT_STICKY
    }

    private fun buildCallShieldNotification(callerNumber: String, callState: String): Notification {
        val tapIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("OPEN_SCREEN", "CALL_SHIELD")
            putExtra("CALLER_NUMBER", callerNumber)
        }

        val tapPendingIntent = PendingIntent.getActivity(
            this,
            101,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(this, CallShieldService::class.java).apply {
            action = ACTION_STOP_CALL_GUARD
        }
        val dismissPendingIntent = PendingIntent.getService(
            this,
            102,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val isRinging = callState == "RINGING"
        val title = if (isRinging) "🚨 NEXUS Call Shield: Incoming Call" else "🛡️ NEXUS Active Call Guard"
        val content = if (isRinging) {
            "Incoming call from $callerNumber • Tap to verify AI voice"
        } else {
            "Call connected with $callerNumber • Real-time acoustic screening active"
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .setColor(0xFF00E5FF.toInt())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setAutoCancel(false)
            .setVibrate(longArrayOf(0, 300, 200, 300))
            .setContentIntent(tapPendingIntent)
            .setFullScreenIntent(tapPendingIntent, true)
            .addAction(
                android.R.drawable.ic_menu_search,
                "🛡️ Verify Voice",
                tapPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Dismiss",
                dismissPendingIntent
            )
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "NEXUS Call Shield Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Real-time incoming call AI voice authenticity notifications"
                enableLights(true)
                lightColor = Color.CYAN
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
