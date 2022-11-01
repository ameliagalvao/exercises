package com.miigubymia.trackdelivery.tracking

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.miigubymia.trackdelivery.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LocationService:Service() {

    //Variavel para pegar as coordenadas
    var coordenates = ""

    // escopo vinculado ao nosso serviço
    // O supervisor garante que se um job nesse serviço falhar, os outros continuarão
    // rodando.
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // Vamos criar uma instância da nossa interface
    private lateinit var locationClient: LocationClient

    // Como ele não vai vincular o serviço a nada, retorna nulo.
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    // Quando nosso serviço for criado...
    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    // Função do serviço que checa cada intent mandada para o serviço e caso alguma dessas intents
    // tenha uma ação do tipo START, o serviço vai começar, se for do tipo STOP, ele vai parar.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        // Verifica se o serviço está ativo e se a string não está vazia e coloca as
        // coordenadas na variavel coordenadas
        if (isMyServiceRunning(LocationService::class.java, this) && coordenates.isNotEmpty()){
            //Toast.makeText(this, coordenates, Toast.LENGTH_SHORT).show()
            application.getSharedPreferences("SaveLocation", MODE_PRIVATE).edit().putString("CurrentLocation", coordenates).commit()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Quando der o start ele vai criar a notificação e exibir as atualizações de localização
    private fun start(){
        // Criação da notificação
        val notification = NotificationCompat.Builder(this,"location")
            .setContentTitle("Rastreando sua localização...")
            .setContentText("Sua localização é: --calculando--")
            .setSmallIcon(R.drawable.ic_launcher_background)
                // permite que a gente possa deslizar ela para cancelar
            .setOngoing(true)

        // vamos usar ele para atualizar as notificações existentes
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // inicia o callback para pegar as localizações
        locationClient.getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                // passa os dados para a variavel
                coordenates = "$lat, $long"
                val updateNotification = notification.setContentText(
                    "Sua localização é: ($lat, $long)"
                )
                notificationManager.notify(1, updateNotification.build())
            }
            .launchIn(serviceScope)

        // para que ele comece no segundo plano
        startForeground(1, notification.build())
    }

    private fun stop(){
        stopForeground(true)
        stopSelf()
    }

    // Quando o serviço for destruído o escopo da corrotina será destruído
    // Assim que cancelarmos o escopo, ou seja, que o serviço for destruído,
    // automaticamente vai parar de acompanhar a localização.
    // Evita vazamentos.
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    // Informações para o serviço informando se deve começar a enviar ou parar.
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}
