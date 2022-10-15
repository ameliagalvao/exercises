package com.miigubymia.noteapp.Room

import androidx.room.*
import com.miigubymia.noteapp.Model.Note
import kotlinx.coroutines.flow.Flow

// A DAO DEVE ser uma classe abstrata, por isso geralmente é criada como uma interface
// Ele deve ser desse tipo porque ela irá armazenar as funções que usaremos nas sessões.
// Serão funções anotadas e o database vai gerar elas no background.

@Dao
interface NoteDAO {
    // vai atribuir o modificador suspend às queries que permitirá
    // chamarmos elas por uma corroutine ou suspenction function.
    // A corrotina previne que a main thread seja usada pelas queries.
    @Insert
    suspend fun insert(note:Note)
    @Update
    suspend fun update(note:Note)
    @Delete
    suspend fun delete(note:Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    //Quando formos criar algum processo como query precisamos anota isso
    // vai pegar tudo na tabela e ordenar do menor para o maior pelo id.
    @Query("SELECT * FROM note_table ORDER BY id ASC")
    // quando a gente faz mudanças no database precisamos mostrar o database para o usuário,
    //para isso usamos o flow. Esse flow será convertido em livedata.
    // Nesse aqui não usamos o suspend porque o Room já executa o flow sem usar o main thread.
    fun getAllNotes():Flow<List<Note>>

}