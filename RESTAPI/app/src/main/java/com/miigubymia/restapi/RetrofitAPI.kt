package com.miigubymia.restapi

import retrofit2.Call
import retrofit2.http.GET

// Ela vai converter o REST API em Kotlin
interface RetrofitAPI {
    // vai retornar os dados da API num array
    // Para fazer isso precisamos enviar um request para o servidor, o qual
    // vai retornar os dados e o REST API.
    // Vamos fazer uma operação de leitura, por isso devemos usar o método GET, já para enviar
    // dados para o servidor seria o POST
    // Se quisermos fazer requests diferentes vamos precisar criar métodos separados e especificar os
    // parâmetros com o query
    @GET("/posts")
    fun getAllPosts():Call<List<Posts>>

}