package com.miigubymia.pbandroidtp5

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivityViewModel: ViewModel() {

    lateinit var person: Person
    private val utils = Utils()

    fun onClickGetIMC(weight:EditText, height:EditText, result:TextView, context: Context){
        if (utils.validator(weight, height)) {
            person = utils.createPerson(weight, height)
            person.getBodyMassIndex(person, person.weight, person.height)
            result.text = "O seu IMC é ${BigDecimal(person.bodyMassIndex).setScale(2, RoundingMode.HALF_UP)}"
        } else {
            Toast.makeText(
                context,
                "Por favor, preencha todos os campos.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}