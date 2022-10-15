package com.miigubymia.androidfeatures

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miigubymia.androidfeatures.databinding.ActivityMainBinding
import com.miigubymia.androidfeatures.makeCall.MakeACallActivity
import com.miigubymia.androidfeatures.microphone.MicrophoneActivity
import com.miigubymia.androidfeatures.sendEMAIL.SendEmailActivity
import com.miigubymia.androidfeatures.sendSMS.SendSMSActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnGoToSendSMS.setOnClickListener {
            val intent = Intent(this@MainActivity, SendSMSActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToSendEmail.setOnClickListener {
            val intent = Intent(this@MainActivity, SendEmailActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToCall.setOnClickListener {
            val intent = Intent(this@MainActivity, MakeACallActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToMicrophone.setOnClickListener {
            val intent = Intent(this@MainActivity, MicrophoneActivity::class.java)
            startActivity(intent)
        }

    }

}