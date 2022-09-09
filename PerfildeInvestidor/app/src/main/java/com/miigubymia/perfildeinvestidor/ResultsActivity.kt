package com.miigubymia.perfildeinvestidor

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.miigubymia.perfildeinvestidor.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultsBinding
    var sharedData = SharedPreferencesFunctions()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var userName = binding.tvNameUser

        var mapAnswers = sharedData.retrieveAnswers(this)
        if (mapAnswers != null) {
            val message = ", seu perfil como investidor é:"
            userName.text = mapAnswers.getValue("InvestorName") as String + message
        }
        mapAnswers?.remove("InvestorName")
        var list = mutableListOf<Int>()
        var answersValues = mapAnswers!!.values
        answersValues.forEach { question ->
            list.add(question as Int)
        }
        val result = list.sum()
        var profile = ""
        when{
            result <= 12 -> profile = "Conservador"
            result <= 29 -> profile = "Moderado"
            result >= 30 -> profile = "Arrojado"
        }
        binding.tvResults.text = profile
        binding.tvreason.text = "Uma vez que você marcou $result pontos."
    }
    override fun onBackPressed() {
        val intent = Intent(this@ResultsActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        sharedData.deleteAnswers(this)
    }
}