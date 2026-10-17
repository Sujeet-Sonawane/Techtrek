package com.nexus.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.nexus.app.service.CallShieldService

class CallBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) 
                ?: "Incoming Caller"

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Incoming phone call ringing - launch Call Shield Alert & HUD notification
                    CallShieldService.startCallGuard(context, incomingNumber, "RINGING")
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Call connected / answered - initiate live voice screening
                    CallShieldService.startCallGuard(context, incomingNumber, "OFFHOOK")
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Call ended / hung up - dismiss HUD and finalize report
                    CallShieldService.stopCallGuard(context)
                }
            }
        }
    }
}
