package com.miigubymia.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.miigubymia.firebase.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var  loginBinding: ActivityLoginBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginBinding.btnSignIN.setOnClickListener {
            val userEmail = loginBinding.etEmailLogin.text.toString()
            val userPassword = loginBinding.etPasswordLogin.text.toString()
            signInFirebase(userEmail, userPassword)
        }

        loginBinding.btnSignUP.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginBinding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetActivity::class.java)
            startActivity(intent)
        }

        loginBinding.btnSignPhone.setOnClickListener {
            val intent = Intent(this@LoginActivity, PhoneActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun signInFirebase(userEmail:String, userPassword:String){
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Vai fazer o reconhecimento do usuário na inicialização
    override fun onStart() {
        super.onStart()
        // Objeto da classe user do Firebase
        // o auth mantém as informações do usuário até que faça o logout
        // Se o usuário estiver logado, o objeto usuário terá um valor
        val user = auth.currentUser
        if (user != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}