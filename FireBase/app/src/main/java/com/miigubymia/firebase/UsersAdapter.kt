package com.miigubymia.firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.firebase.databinding.ItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

// vamos usar o construtor para enviar os dados criados na main activity para esta classe
// Para poder usar o intent a gente precisa de um parâmetro de contexto que contenha as propriedades da activity
class UsersAdapter(var context: Context, var userList:ArrayList<Users>): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    // como estamos usando viewBinding temos que usar binding no construtor do UsersViewHolder
    // o binding tá vindo lá do Layout que criamos para os itens, por isso o nome dele [e o nome do layout
    inner class UsersViewHolder(val adapterBinding: ItemBinding) :   RecyclerView.ViewHolder(adapterBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        //Se a gente não estivesse usando o viewBinding iriamos criar um objeto da classe view
        // e transferir o design para esse objeto.
        // No entanto, como usamos o binding, vamos usar o inflate com três parâmetros:
        //O primeiro é o layout inflater.from com o contexto, o segundo é o parent e o terceiro false.
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Como definimos na classe UsersViewHolder que ela vai pegar um ItemBinding de parâmetro, temos
        // que colocar aqui
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        // vamos transferir os dados que estão no Array para o componente.
        // Não temos como acessar diretamente os textView components aqui, por isso
        // precisamos usar um objeto holder
        holder.adapterBinding.tvName.text = userList[position].userName
        holder.adapterBinding.tvAge.text = userList[position].userAge.toString()
        holder.adapterBinding.tvmail.text = userList[position].userEmail

        // colocamos o url numa variável
        val imageUrl = userList[position].url
        // Carrega a imagem usando o Picasso e o url
        // Vamos usar o callback do picasso para mostrar o progressBar enquanto a img não foi carregada.
        Picasso.get().load(imageUrl).into(holder.adapterBinding.ivProfilePhoto, object :Callback{
            override fun onSuccess() {
                //se a imgm for carregada com sucesso, a pbar deve ficar invisível
                holder.adapterBinding.pbarCardView.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context, e?.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

        // para tornar o linear layout inerente a cada item clicável e enviar os dados
        // para a próxima activity
        holder.adapterBinding.linearlayout.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("UserID", userList[position].userId)
            intent.putExtra("name", userList[position].userName)
            intent.putExtra("age", userList[position].userAge)
            intent.putExtra("email", userList[position].userEmail)
            intent.putExtra("imageUrl", imageUrl)
            intent.putExtra("imageName", userList[position].imageName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        //números de elementos do ArrayList
        return userList.size
    }

    // método para identificar o id do usuário a partir do parâmetro posição que vamos passar.
    fun getUserId(position: Int): String{
        return userList[position].userId
    }

    // função para pegar o nome da imagem através da posição
    fun getImageName(position:Int):String{
        return userList[position].imageName
    }

}