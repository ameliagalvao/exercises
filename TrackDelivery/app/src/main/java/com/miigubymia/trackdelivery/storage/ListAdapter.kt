package com.miigubymia.trackdelivery.storage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.trackdelivery.MainActivity
import com.miigubymia.trackdelivery.R

class ListAdapter(val registers: ArrayList<String> = arrayListOf<String>()): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private lateinit var fileListener: onFileClickListener
    interface onFileClickListener{
        fun onFileClick(position:Int)
    }
    fun setOnFileClickListener(listener: onFileClickListener){
        fileListener = listener
    }

    class ListViewHolder(itemView: View, listener: onFileClickListener):RecyclerView.ViewHolder(itemView){
        val tvFileName: TextView = itemView.findViewById(R.id.tvFileName)
        init {
            itemView.setOnClickListener {
                listener.onFileClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_register_card,parent,false)
        return ListViewHolder(view, fileListener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var currentFile:String = registers[position]
        holder.tvFileName.text = currentFile
    }

    override fun getItemCount(): Int {
        return registers.size
    }
}