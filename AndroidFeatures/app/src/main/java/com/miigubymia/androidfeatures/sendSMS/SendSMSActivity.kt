package com.miigubymia.androidfeatures.sendSMS

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.miigubymia.androidfeatures.databinding.ActivitySendSmsactivityBinding

// NÃO TÁ FUNCIONANDO!!!!!!!

class SendSMSActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendSmsactivityBinding
    var userMessage: String = ""
    var userNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendSmsactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSendSMS.setOnClickListener {
            userMessage = binding.etMessageSMS.text.toString()
            userNumber = binding.etPhoneSMS.text.toString()
            sendSMS(userMessage, userNumber)
        }

    }

    fun sendSMS(userMessage:String, userNumber:String){
        // Checa se o usuário deu a permissão ou não para o envio de SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED){
            // Pedir a permissão do usuário
            // O segundo parâmetro é um array com todas as permissões que precisamos pedir ao usuário
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        } else {
            val smsManager = this.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(userNumber, null, userMessage, null, null)
            Toast.makeText(applicationContext, "Mensagem enviada", Toast.LENGTH_SHORT).show()
        }
    }
    // Esse método gerencia o resultado da decisão do usuário, caso ele tenha concedido a permissão
    // no bloco do if na função sendSMS
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val smsManager = this.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(userNumber, null, userMessage, null, null)
            Toast.makeText(applicationContext, "Mensagem enviada", Toast.LENGTH_SHORT).show()
        }
    }

}