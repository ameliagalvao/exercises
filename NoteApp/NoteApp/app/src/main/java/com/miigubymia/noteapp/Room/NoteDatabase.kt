package com.miigubymia.noteapp.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.miigubymia.noteapp.Model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Essa classe faz a conexão entre a entity e o DAO.
// se tivesse mais de uma tabela deveríamos colocar uma vírgula e
//escrever dentro das chaves.
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase:RoomDatabase(){
    // a função mão tem corpo, porque o próprio room vai fazer o que tem de ser feito.
    abstract fun getNoteDao():NoteDAO

    //Precisamos prevenir que sejam criados mais de um objeto do mesmo database,
    // para isso vamos usar o Singleton
    // o companion permite que a gente acesse a instância criada aqui a partir
    // de qualquer lugar na aplicação
    companion object{
        // O Volatile faz com que a instância seja visível a todas as threads
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context:Context,scope: CoroutineScope):NoteDatabase{
            // o ?: representa o else
            // Se mais de uma thread tentar criar uma instância do database
            // ao mesmo tempo, ele irá bloquear, só deixando a criação de uma por vez.
            return INSTANCE ?: synchronized(this){
                // criando a instância do database:
                val instance = Room.databaseBuilder(context.applicationContext,
                NoteDatabase::class.java,"note_database")
                    .addCallback(NoteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // a palavra sincronized previne isso de criar mais de uma instância ao mesmo tempo
                instance
            }
        }
    }
    // Precisamos criar uma corrotina do escopo para poder criar adicionar os dados
    // Para isso precisamos criar um objeto da classe routine scope aqui no construtor
    // assim podemos usar o insert com o scope.
    private class NoteDatabaseCallback(private val scope:CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Primeiro vamos checar a intância, se ela não for nula, podemos adicionar data ao db.
            // ao invés de usar o it padrão, podemos usar um lambda chamado database para representar o db
            INSTANCE?.let{ database ->
                scope.launch {
                    // vamos primeiro criar uma variável para acessar todas as funções na interface DAO
                    val noteDao = database.getNoteDao()
                    // agora podemos usar o insert
                    noteDao.insert(Note("Title 1", "Description 1"))
                    noteDao.insert(Note("Title 2", "Description 2"))
                    noteDao.insert(Note("Title 3", "Description 3"))
                    // depois disso precisamos chamar a função callback dentro
                    // da função getdatabase depois to room.databasebuilder
                }
            }
        }
    }
}