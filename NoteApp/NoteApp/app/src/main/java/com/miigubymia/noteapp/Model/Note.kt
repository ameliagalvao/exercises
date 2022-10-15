package com.miigubymia.noteapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Para informar ao room que essa classe é do tipo Entity, basta usar o anotation abaixo
// Ao fazermos essa declaração o room já vai criar automaticamente uma tabela no banco de dados
@Entity(tableName = "note_table")
class Note(val titel:String, val description:String) {

    // Para que a chave seja única usamos o primary key e para que
    // ela seja gerada automaticamente usamos o autoGenerate
    @PrimaryKey(autoGenerate = true)
    var id = 0
}