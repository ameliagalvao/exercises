package com.miigubymia.androidfeatures.microphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.miigubymia.androidfeatures.databinding.ActivityMicrophoneBinding
import java.util.*
import kotlin.collections.ArrayList

class MicrophoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMicrophoneBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMicrophoneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //precisa ser registrado no onCreate
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK && data != null){
                    val speakReult: ArrayList<String> = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                    binding.tvResult.text = speakReult[0]
                }
            })

        binding.btnMicrophone.setOnClickListener{
            convertSpeech()
        }
    }
    fun convertSpeech(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        // define o idioma do reconhecimento de voz
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        activityResultLauncher.launch(intent)
    }
}