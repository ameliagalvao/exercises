package com.miigubymia.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.miigubymia.quizgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(this@MainActivity, QuizActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignOut.setOnClickListener {
            //Esse método apenas faz o logout para o email e senha.
            FirebaseAuth.getInstance().signOut()
            // Esse outro  é para o do google
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Sucess!", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}