package com.miigubymia.androidfeatures.sendEMAIL

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miigubymia.androidfeatures.databinding.ActivitySendEmailBinding

class SendEmailActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySendEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendEmailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSendEmail.setOnClickListener {
            val userAdress = binding.etEmailAddress.text.toString()
            val userSubject = binding.etEmailSubject.text.toString()
            val userMessage = binding.etEmailMessage.text.toString()
            sendEmail(userAdress, userSubject, userMessage)
        }

    }

    fun sendEmail(userAddress:String, userSubject:String, userMessage:String){
        val emailAddresses = arrayOf(userAddress)
        // vamos criar um intent para levar os dados quando formos para a alicação do email
        // para emails usamos o sendto, se fosse para outro meio de comunicação seria send
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        // para que o endereço preencha o campo
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddresses)
        //Para o assunto
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, userSubject)
        // mensagem
        emailIntent.putExtra(Intent.EXTRA_TEXT, userMessage)
        // não podemos iniciar a intent direto porque pode não existir uma aplicação capaz
        // de processar ela no aplicativo.
        if (emailIntent.resolveActivity(packageManager) != null){
            startActivity(Intent.createChooser(emailIntent, "Choose an app"))
        }
    }

}