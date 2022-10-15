package com.miigubymia.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.miigubymia.quizgame.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()
            signupWithFirebase(email, password)
        }
    }

    fun signupWithFirebase(email:String, password:String){
        binding.pbarSignUp.visibility = View.VISIBLE
        binding.btnSignUp.isClickable = false
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Created.", Toast.LENGTH_SHORT).show()
                finish()
                binding.pbarSignUp.visibility = View.INVISIBLE
                binding.btnSignUp.isClickable = true
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}