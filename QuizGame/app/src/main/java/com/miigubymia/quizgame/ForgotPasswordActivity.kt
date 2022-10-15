package com.miigubymia.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.miigubymia.quizgame.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnForgotReset.setOnClickListener {
            val userEmail = binding.etEmailSForgot.text.toString()
            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Sent to email", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}