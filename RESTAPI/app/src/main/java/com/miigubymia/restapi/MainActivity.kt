package com.miigubymia.restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.miigubymia.restapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val baseURL = "https://jsonplaceholder.typicode.com/"
    var postsList = ArrayList<Posts>()
    lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Como o recycler view vai mostrar os itens
        binding.rvPosts.layoutManager = LinearLayoutManager(this)

        showPosts()
    }

    fun showPosts(){
        val retrofit = Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val call:Call<List<Posts>> = retrofitAPI.getAllPosts()
        // É graças a essa função que vamos conseguir interagir com o servidor de forma assíncrona.
        call.enqueue(object : Callback<List<Posts>>{
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                // se não houver dados ou se a operação for nula...
                if (response.isSuccessful){
                    binding.progressBar.isVisible = false
                    binding.rvPosts.isVisible = true
                    postsList = response.body() as ArrayList<Posts> /* = java.util.ArrayList<com.miigubymia.restapi.Posts> */
                    adapter = PostsAdapter(postsList)
                    binding.rvPosts.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

}