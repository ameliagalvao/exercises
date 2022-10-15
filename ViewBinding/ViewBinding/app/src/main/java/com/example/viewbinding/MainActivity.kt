package com.example.viewbinding

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.viewbinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    var count:Int = 0
    // não dá para usar lateinit em tipos primitivos.
    lateinit var sharedPreferenceExample: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fazendo o binding a gente não precisa mais usar o find view by id!! Passamos a usar
        // mainBinding.nomedoview
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        // ao usar o binding devemos substituir o caminho para o activity_main com o view.
        setContentView(view)

        mainBinding.buttonSend.setOnClickListener {
            mainBinding.textViewTest.text = mainBinding.editTextName.text.toString()
            }

        mainBinding.buttonCount.setOnClickListener {
            count += 1
            mainBinding.buttonCount.text = count.toString()

        }
    }


    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        retreiveData()
    }

    fun saveData(){
        // o modo privado permite gravar dados de qualquer lugar da aplicação sem deixar que
        // aplicações de fora tenham acesso a esses dados.
        sharedPreferenceExample = this.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        //função da classe sharedpreferences que vai salvar os dados
        val editor = sharedPreferenceExample.edit()
        editor.putString("key name", mainBinding.editTextName.text.toString())
        editor.putString("key message", mainBinding.editTextTextMultiLine.text.toString())
        editor.putInt("key count", count)
        editor.putBoolean("key isCheked", mainBinding.checkBox.isChecked!!)
        editor.apply()
        Toast.makeText(applicationContext, "Dados salvos.", Toast.LENGTH_LONG).show()

    }

    fun retreiveData(){
        sharedPreferenceExample = this.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        mainBinding.editTextName.setText(sharedPreferenceExample.getString("key name", null))
        mainBinding.editTextTextMultiLine.setText(sharedPreferenceExample.getString("key message", null))
        mainBinding.buttonCount.setText("" + sharedPreferenceExample.getInt("key count", 0))
        mainBinding.checkBox.isChecked = sharedPreferenceExample.getBoolean("key isCheked", false)!!
    }
}