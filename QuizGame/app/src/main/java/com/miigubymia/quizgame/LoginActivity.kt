package com.miigubymia.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.miigubymia.quizgame.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerActivityForGoogleSignIn()

        binding.btnSignIn.setOnClickListener {
            val userEmail = binding.etEmail.text.toString()
            val userPassword = binding.etPassword.text.toString()
            signInUser(userEmail, userPassword)
        }

        // vamos acessar o textView do botão do google para mudar algumas coisas
        val googleBtn = binding.btnGoogleSignIn.getChildAt(0) as TextView
        googleBtn.text = "Continue with Google"
        googleBtn.setTextColor(Color.BLACK)
        googleBtn.textSize = 18F

        binding.btnGoogleSignIn.setOnClickListener {
            signInGoogle()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            // Nesse caso não podemos usar o finish porque a SignUp é filha do Login, assim
            // se fecharmos o login e o usuário pressionar para voltar à página anterior, o
            // app vai crashar porque fechamos a página para a qual ele deveria voltar.

        }

        binding.tvPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun registerActivityForGoogleSignIn() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == RESULT_OK && data != null){
                val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                firebaseSignInWithGoogle(task)
            }
        })
    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account:GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch (e:ApiException){
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        // o idToken é único para cada dispositivo e graças a ele conseguimos dientificar cada dispositivo.
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                //val user = firebaseAuth.currentUser

            }else{

            }
        }
    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1037142835866-ffa662okt41us3ib56duhhr48ripdrvp.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private fun signIn() {
        val signInIntent:Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    fun signInUser(userEmail:String, userPassword:String){
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Quando a aplicação é iniciada essa função checa se o usuário está logado ou não.
    override fun onStart() {
        super.onStart()
        // método para pegar informações sobre o usuário
        val user = firebaseAuth.currentUser
        if (user != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}