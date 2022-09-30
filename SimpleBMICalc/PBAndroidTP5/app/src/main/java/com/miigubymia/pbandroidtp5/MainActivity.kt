package com.miigubymia.pbandroidtp5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.miigubymia.pbandroidtp5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var weight = binding.etWeight
        var height = binding.etHeight
        var result = binding.tvResult

        binding.btnCalculate.setOnClickListener {
            viewModel.onClickGetIMC(weight, height, result, this@MainActivity)
        }
    }
}