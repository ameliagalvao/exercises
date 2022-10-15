package com.miigubymia.noteapp

import android.app.Application
import com.miigubymia.noteapp.Repository.NoteRepository
import com.miigubymia.noteapp.Room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApplication:Application() {
    //Como devemos criar apenas uma instância do db e do repository,
    // criamos uma nova classe de aplicação, a qual herdou da classe aplicação.
    // Para isso também criamos um callback em que sobreescrevemos a função
    // on create no note database para adicionar dados por padrão
    // Como não podíaos usar UI threads para as operações do bd, usamos corrotinas.
    val applicationScope = CoroutineScope(SupervisorJob())

    //vamos criar instâncias do db e do
    // o by lazy é uma propriedade de delegação usada no Kotlin
    // Quando o usamos, uma instância do db só será criada quando for necessária
    // e não assim que o applicativo rodar.
    // Como criamos o getDatabase no companion object da classe NoteDatabase, podemos acessá-lo
    // por aqui diretamente.
    val database by lazy { NoteDatabase.getDatabase(this,applicationScope) }
    //vamos acessar a função noteDAO através do objeto database
    val repository by lazy { NoteRepository(database.getNoteDao()) }
}