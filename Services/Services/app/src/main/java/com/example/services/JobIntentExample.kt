package com.example.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
// está em desuso, hoje utiliza-se o work manager por maior compatibilidade.

class JobIntentExample: JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        Log.d("Service", "Job Intent started")
        Log.d("Service", Thread.currentThread().name)
    }
    // escopo que permite acesso direto à função através do main activity, se não seria necessãrio criar um
    // objeto para poder usar a função.
    companion object{
        fun myBackgroundService(context: Context, intent: Intent){
            enqueueWork(context, JobIntentExample::class.java, 1,intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Service", "Job Intent is stopped.")
    }
}