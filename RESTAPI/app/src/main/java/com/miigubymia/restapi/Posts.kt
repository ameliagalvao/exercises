package com.miigubymia.restapi

import com.google.gson.annotations.SerializedName

data class Posts(
    // Quando os nomes da API e daqui forem iguais a gente não precisará usar annotattion, mas
    // se forem diferentes vamos precisar senão o retrofit não reconhecerá.
    val userId:Int,
    val id:Int,
    val title:String,

    @SerializedName("body")
    val subtitle:String,
) {
}