package com.miigubymia.noteapp.View

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.noteapp.Adapters.NoteAdapter
import com.miigubymia.noteapp.NoteApplication
import com.miigubymia.noteapp.R
import com.miigubymia.noteapp.ViewModel.NoteViewModel
import com.miigubymia.noteapp.ViewModel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel:NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView:RecyclerView = findViewById(R.id.rvNoteList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteAdapter = NoteAdapter()
        recyclerView.adapter = noteAdapter

        val viewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)

        //O primeiro parâmetro é a activity em que ele vai rodar, o segundo
        noteViewModel = ViewModelProvider(this,viewModelFactory).get(NoteViewModel::class.java)

        noteViewModel.myAllNotes.observe(this, Observer {
            // update UI
            noteAdapter.setNote(it)
        })
    }

    // layout do menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menu,menu)
        return true
    }
    // quando clicar no botão de adicionar ele vai chamar a activity para enviar um nova nota
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemAddNote -> {
                val intent = Intent(this,NoteAddActivity::class.java)
                startActivity(intent)
            }
            R.id.itemDeleteAll -> Toast.makeText(applicationContext,"Deletou",Toast.LENGTH_SHORT).show()
        }
        return true
    }
}