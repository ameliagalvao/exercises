package com.miigubymia.trackdelivery.tracking

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// Transformamos nosso callBack numa classe com uma única responsabilidade e uma boa abstração
class DefaultLocationClient(
    private val context:Context,
    // pega a localização do usuário
    private val client: FusedLocationProviderClient
):LocationClient {

    private lateinit var locationClient: LocationClient

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        // Primeiro vamos checar se o usuário aceitou a permissão
        // O callbackFlow é usado quando temos um callback que queremos transformar num flow
        // Nesse caso temos um callback da nossa localização que vai ficar retornando de tempo
        // em tempo e queremos transformar num flow
        // Outra questão é que o callbackFlow também pode ser usado para modelar um callback
        // que tenha um ciclo de vida, como é o caso do nosso, pois temos duas funções que
        // informam ao LocationClient quando iniciar ou parar de acompanhar a localização.
        // Para modelar vamos usar as corrotinas.
        return callbackFlow {
            if(!context.hasLocationPermission()){
                throw LocationClient.LocationException("Sem a permissão de localização")
            }
            // Existindo a permissão, vamos checar se há recursos para usar o gps e a internet
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            // Se não tivermos os dois
            if(!isGpsEnabled && !isNetworkEnabled){
                throw LocationClient.LocationException("GPS desabilitado.")
            }

            //Configurações da localização
            val request = LocationRequest.create()
                .setInterval(interval)
                    //não vai ser mais rápido que esse
                .setFastestInterval(interval)

            val locationCallback = object : LocationCallback(){
                // A função abaixo vai ser chamada sempre que o fuseLocationProviderClient
                // mandar uma nova localização
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    // o resultado contém uma lista com as localizações
                    // queremos o último elemento dessa lista, que é o mais novo
                    result.locations.lastOrNull()?.let { location ->
                        // vamos notificar o flow e enviar algo nele para pegarmos essa localização
                        // acima. Assim o lauch abaixo vai ser sempre executado para
                        // cada localização que pegarmos
                        launch { send(location) }
                    }
                }
            }

            // Começa a pedir atualizações ao cliente
            // Aqui é o método que vai solicitar a atualização de localização
            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            // Vai bloquear o flow até que o escopo da corritina seja terminado
            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}