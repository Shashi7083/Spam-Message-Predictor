package com.example.spamdetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spamdetector.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var apiKey : String = ""


        binding.btnApi.setOnClickListener {

            if(binding.etApi.text.isEmpty() || binding.etApi.text ==null){
                binding.etApi.error = "Provide Api Key "
            }else{
                binding.etApi.error = null
                apiKey = binding.etApi.text.toString()
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("api",apiKey)
                startActivity(intent)
            }
        }

    }
}