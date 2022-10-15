package com.miigubymia.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.miigubymia.firebase.databinding.ActivityPhoneBinding
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {
// Rever código, que não funcionou.
    lateinit var binding: ActivityPhoneBinding
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mCallbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSendSMS.setOnClickListener {
            val userPhoneNumber = binding.etPhoneSMS.text.toString()
            // função para fazer a autenticação
            // o newBuilder precisa de um objeto do tipo FirebaseAuth
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(userPhoneNumber)
                    // o L é de long
                .setTimeout(60L, TimeUnit.SECONDS)
                    //definimos em qual activity vamos verificar o código
                .setActivity(this@PhoneActivity)
                    //
                .setCallbacks(mCallbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        binding.btnVerify.setOnClickListener {
            signInWithSMS()
        }
        // como é uma classe abstrata, que não permite a criação de um objeto de forma direta,
        // precisamos usar o object para criar o objeto
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            // o que irá acontecer quando o código for verificado com sucesso
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }
            // se a verificação falhar...
            override fun onVerificationFailed(p0: FirebaseException) {
                TODO("Not yet implemented")
            }
            // Acrescentou esse método para pegar o código e transferir para outra variável,
            // assim podemos usar o código onde quisermos
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                // Como o código é o p0, vamos armazenar ele nessa variável global para
                // poder usá-lo
                verificationCode = p0
            }

        }

    }

    fun signInWithSMS(){
        val userEnterCode = binding.etVerification.toString()
        //Determina os critérios para o login acontecer:
        val credential = PhoneAuthProvider.getCredential(verificationCode, userEnterCode)
        signInWithPhoneAuthCredential(credential)
    }

    // Realiza o login de acordo com o critério acima
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent = Intent(this@PhoneActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}