package com.miigubymia.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.miigubymia.quizgame.databinding.ActivityMainBinding
import com.miigubymia.quizgame.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Animação
        val alphaAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash_anim)
        binding.tvTitleWelcome.startAnimation(alphaAnimation)

        // Criamos um obj handler para que no tempo programado ele feche a atividade e inicie a
        // próxima.
        val handler = Handler(Looper.getMainLooper())
        //Função do handler que vai esperar certo tempo para realizar o estabelecido
        // Como o runnable é uma interface vamos ter que usar o object para criar um objeto dela
        handler.postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 5000)
    }
}