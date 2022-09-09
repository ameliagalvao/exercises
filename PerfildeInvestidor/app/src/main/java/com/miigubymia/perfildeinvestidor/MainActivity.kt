package com.miigubymia.perfildeinvestidor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.miigubymia.perfildeinvestidor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var data:SharedPreferencesFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        data = SharedPreferencesFunctions()

        binding.btnStart.setOnClickListener {
            if(binding.etName.text.isEmpty()){
                Toast.makeText(this@MainActivity,"Por favor, preencha seu nome.", Toast.LENGTH_SHORT).show()
            }else{
            val intent = Intent(this@MainActivity, QuestionsActivity::class.java)
            startActivity(intent)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        data.saveString(this@MainActivity,binding.etName.text.toString())
    }

}