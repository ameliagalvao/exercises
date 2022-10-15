package com.miigubymia.noteapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.noteapp.Model.Note
import com.miigubymia.noteapp.R

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var notes:List<Note> = ArrayList()

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val cardView: CardView = itemView.findViewById(R.id.cdItemList)
    }

    // vinculando o layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item,parent,false)
        return NoteViewHolder(view)
    }

    // O que vai mostrar em cada cardview
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        var currentNote:Note = notes[position]
        holder.textViewTitle.text = currentNote.titel
        holder.textViewDescription.text = currentNote.description
    }

    // quantos itens queremos mostrar
    override fun getItemCount(): Int {
        return notes.size
    }

    // vamos fazer a conexão do adapter através do live
    fun setNote(myNotes:List<Note>){
        this.notes = myNotes
        notifyDataSetChanged()
    }
}