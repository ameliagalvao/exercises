package com.miigubymia.trackdelivery.storage

import android.app.Activity
import android.app.Service
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListViewModel:ViewModel() {

    fun addToList(fileName:String, registers:MutableList<String>){
        registers.add(fileName)
    }

    fun updateListInSharedPref(activity: Activity, registers:MutableList<String>){
        val gson = Gson()
        val json = gson.toJson(registers)
        // SharedPref
        activity.getSharedPreferences("FilesList", Service.MODE_PRIVATE).edit().putString("Registers", json).commit()
    }

    fun getListFromSharedPref (activity: Activity): ArrayList<String>{
        val text = arrayListOf<String>("Sem registros.")
        val gson = Gson()
        val default = gson.toJson(text)
        val json = activity.application.getSharedPreferences("FilesList",
            Service.MODE_PRIVATE
        ).getString("Registers", default)
        val type = object : TypeToken<ArrayList<String>>(){}.type//converte o json para list
        return gson.fromJson(json,type)//retornando a lista
    }

}