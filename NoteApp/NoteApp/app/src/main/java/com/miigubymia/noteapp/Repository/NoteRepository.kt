package com.miigubymia.noteapp.Repository

import androidx.annotation.WorkerThread
import com.miigubymia.noteapp.Model.Note
import com.miigubymia.noteapp.Room.NoteDAO
import kotlinx.coroutines.flow.Flow

// Como nosso objetivo é acessar o DAO e não o database inteiro, vamos criar
// um construtor da interface do DAO. Não há necessidade de expor todos o banco de dados
// ao repositório, pois o DAO já tem acesso a ele.
class NoteRepository(private val noteDao:NoteDAO) {
    // o flow observa mudanças no banco
    // armazena as notas nessa lista:
    val myAllNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Essa anottation permite que as opraçoes sejam feito numa thread única.
    @WorkerThread
    suspend fun insert(note:Note){
        noteDao.insert(note)
    }

    @WorkerThread
    suspend fun update(note:Note){
        noteDao.update(note)
    }

    @WorkerThread
    suspend fun delete(note:Note){
        noteDao.delete(note)
    }

    @WorkerThread
    suspend fun deleteAllNotes(){
        noteDao.deleteAllNotes()
    }
}