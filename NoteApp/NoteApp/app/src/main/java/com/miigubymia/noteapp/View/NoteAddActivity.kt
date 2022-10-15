package com.miigubymia.noteapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miigubymia.noteapp.R
import com.miigubymia.noteapp.databinding.ActivityNoteAddBinding

class NoteAddActivity : AppCompatActivity() {

lateinit var binding:ActivityNoteAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCancel.setOnClickListener {  }
        binding.btnSave.setOnClickListener {  }
    }
}