package com.miigubymia.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.miigubymia.firebase.databinding.ActivityForgetBinding

class ForgetActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgetBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnResetForget.setOnClickListener {
            val email = binding.etEmailForget.text.toString()
            //envia automaticamente um e-mail para o usuário resetar a senha
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Sent.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }
}