package com.miigubymia.perfildeinvestidor

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable


class SharedPreferencesFunctions {

    fun saveString(context: Context, string: String){
        val sharedPreferenceExample: SharedPreferences =
            context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = sharedPreferenceExample.edit()
        editor.putString("InvestorName", string)
        editor.apply()
    }

    fun saveInt(context: Context, int: Int, title:String){
        val sharedPreferenceExample: SharedPreferences =
            context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = sharedPreferenceExample.edit()
        editor.putInt(title, int)
        editor.apply()
    }

    fun retrieveAnswers(context: Context): MutableMap<String, *>? {
        val sharedPreferenceExample: SharedPreferences =
            context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        return sharedPreferenceExample.all
    }

    fun deleteAnswers(context: Context){
        val sharedPreferenceExample: SharedPreferences =
            context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = sharedPreferenceExample.edit()
        editor.clear()
        editor.apply()
    }

}