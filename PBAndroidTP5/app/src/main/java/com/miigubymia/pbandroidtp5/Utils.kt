package com.miigubymia.pbandroidtp5

import android.content.Context
import android.widget.EditText

class Utils {


    fun createPerson(weightEditText: EditText, heightEditText: EditText): Person{
        var person = Person(weightEditText.text.toString().toDouble(), heightEditText.text.toString().toDouble())
        return person
    }

    fun validator(weightEditText: EditText, heightEditText: EditText): Boolean {
        var result: Boolean
        return if (weightEditText.text.isNotEmpty() && heightEditText.text.isNotEmpty()){
            result = true
            result
        } else {
            false
        }
    }


}