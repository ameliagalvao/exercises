package com.example.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadCastExample: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isAirPlaneMode : Boolean = intent!!.getBooleanExtra("state", false)
        if (isAirPlaneMode){
            Toast.makeText(context, "Ligado", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(context, "desligado.", Toast.LENGTH_SHORT).show()
        }
    }
}