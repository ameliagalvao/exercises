package com.miigubymia.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.miigubymia.firebase.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRegisterSignUp.setOnClickListener {
            val userEmail = binding.etEmailSignUp.text.toString()
            val userPassword = binding.etPasswordSignUp.text.toString()
            signUpWithFirebase(userEmail, userPassword)
        }

    }

    fun signUpWithFirebase(userEmail:String, userPassword:String){
        // essa função cria automaticamente um usuário e senha no Firebase
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Created.", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}