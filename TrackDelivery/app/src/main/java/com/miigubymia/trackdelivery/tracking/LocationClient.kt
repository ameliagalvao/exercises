package com.miigubymia.trackdelivery.tracking

import android.location.Location
import kotlinx.coroutines.flow.Flow


// Vai tornar as atualizações de localização abstratas
interface LocationClient {

    // Usamos o flow, porque se trata de algo que vai ser
    // sequencialmente emitido num intervalo de tempo (stream)
    fun getLocationUpdates(interval:Long): Flow<Location>

    // Para quando algo não for como o esperado
    class LocationException(message:String):Exception()
}