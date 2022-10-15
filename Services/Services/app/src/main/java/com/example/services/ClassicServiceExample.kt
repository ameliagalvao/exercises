package com.example.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ClassicServiceExample: Service() {

    //não vamos vincular esse serviço
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    // aqui fica o que deve ser executado quando o serviço for criado
    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "Classic service created.")
    }

    // aqui fica o que deve ser executado quando o serviço começar
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "Classic service is started.")
        Log.d("Service Thread", Thread.currentThread().name)
        // O comando abaixo pausa o serviço automaticamente assim que ele acabar sua tarefa.
        //stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    // aqui fica o que deve ser feito quando o seriço parar
    override fun onDestroy() {
        super.onDestroy()
        Log.d("Service", "Classic service stopped.")
    }
}