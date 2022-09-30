package com.miigubymia.pbandroidtp5

import android.content.Context
import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivityViewModel : ViewModel() {

    lateinit var person: Person
    private val utils = Utils()

    fun onClickGetIMC(
        weight: EditText,
        height: EditText,
        result: TextView,
        context: Context,
        degree: TextView
    ) {
        if (utils.validator(weight, height)) {
            person = utils.createPerson(weight, height)
            person.getBodyMassIndex(person, person.weight, person.height)
            result.text = "O seu IMC é ${
                BigDecimal(person.bodyMassIndex).setScale(
                    2,
                    RoundingMode.HALF_UP
                )
            } e o seu grau de obesidade é:"
            textDegreeColor(person.bodyMassIndex, degree)
            degree.text = "\n${showObesityDegree(person.bodyMassIndex)}"
        } else {
            Toast.makeText(
                context,
                "Por favor, preencha todos os campos.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun textDegreeColor(bmi: Double, textView: TextView) {
        return when {
            //vermelho
            bmi < 17 -> textView.setTextColor(Color.parseColor("#F44336"))
            //amarelo
            bmi < 18.5  -> textView.setTextColor(Color.parseColor("#FFFF9800"))
            //verde
            bmi < 25 -> textView.setTextColor(Color.parseColor("#FF018786"))
            //amarelo
            bmi < 30  -> textView.setTextColor(Color.parseColor("#FFFF9800"))
            //vermelho
            bmi >= 30 -> textView.setTextColor(Color.parseColor("#FF018786"))
            //cinza
            else -> textView.setTextColor(Color.parseColor("#808080"))
        }
    }

    fun showObesityDegree(bmi: Double): String {
        return when {
            bmi < 16.0 -> "Magreza grave"
            bmi < 17.0 -> "Magreza moderada"
            bmi < 18.5 -> "Magreza leve"
            bmi < 25.0 -> "Saudável"
            bmi < 30.0 -> "Sobrepeso"
            bmi < 35.0 -> "Obesidade Grau I"
            bmi < 40.0 -> "Obesidade Grau II (Severa)"
            bmi >= 40.0 -> "Obesidade Grau III (Mórbida)"
            else -> ""
        }
    }


}
