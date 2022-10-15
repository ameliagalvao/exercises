package com.example.services

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var buttonStartClassic:Button
    lateinit var buttonStartJobIntentService:Button
    lateinit var buttonStop:Button
    lateinit var broadCastPlane: BroadCastExample

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonStop = findViewById(R.id.stop)
        buttonStartClassic = findViewById(R.id.startCS)
        buttonStartJobIntentService = findViewById(R.id.startJIS)
        buttonStop.setOnClickListener {
            val intent = Intent(this@MainActivity, ClassicServiceExample::class.java)
            stopService(intent)
        }
        buttonStartClassic.setOnClickListener {
            val intent = Intent(this@MainActivity, ClassicServiceExample::class.java)
            startService(intent)
        }
        buttonStartJobIntentService.setOnClickListener {
            val intent = Intent(this@MainActivity, JobIntentExample::class.java)
            JobIntentExample.myBackgroundService(this@MainActivity, intent)
        }

        broadCastPlane = BroadCastExample()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        this.registerReceiver(broadCastPlane, filter)

    }

    override fun onStart() {
        super.onStart()
        broadCastPlane = BroadCastExample()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        this.registerReceiver(broadCastPlane, filter)
    }


    override fun onStop() {
        super.onStop()
        broadCastPlane = BroadCastExample()
        this.unregisterReceiver(broadCastPlane)
    }
}