package com.miigubymia.androidfeatures.makeCall

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.miigubymia.androidfeatures.databinding.ActivityMainBinding
import com.miigubymia.androidfeatures.databinding.ActivityMakeAcallBinding

class MakeACallActivity : AppCompatActivity() {

    var userNumber: String = ""

    private lateinit var binding: ActivityMakeAcallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeAcallBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCall.setOnClickListener {
            userNumber = binding.etPhoneCall.text.toString()
            startCall(userNumber)
        }

    }

    fun startCall(userNumber: String) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // podemos usar qualquer número para o requestCode. Ele é só um identificador único do request
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 100)
        }else{
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$userNumber")
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$userNumber")
            startActivity(intent)
        }
    }
}