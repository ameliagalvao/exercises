package com.miigubymia.restapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.restapi.databinding.PostsItemBinding

// O ideal deixar a lista no construtor primário para facilitar o envio de dados da main activity
// para a adapter class
class PostsAdapter(var postsList:ArrayList<Posts>): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    // Essa classe representa o design que criamos para os posts e definirá os componentes dele.
    // Por padrão o Android criar o construtor dessa classe como itemView:View, mas como estamos
    // usando view binding, precisamos mudar e referenciar o layout que criamos para os itens.
    inner class PostsViewHolder(val adapterBinding:PostsItemBinding)
        : RecyclerView.ViewHolder(adapterBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        // se não estivéssemos usando o viewBinding, teriamos que criar um objeto da classe view e
        // transferir o design para ele
        val binding = PostsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        // aqui vamos transferir os dados da array para os componentes através do holder
        holder.adapterBinding.tvUserId.text = postsList[position].userId.toString()
        holder.adapterBinding.tvId.text = postsList[position].id.toString()
        holder.adapterBinding.tvTitle.text = postsList[position].title
        holder.adapterBinding.tvBody.text = postsList[position].subtitle
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

}