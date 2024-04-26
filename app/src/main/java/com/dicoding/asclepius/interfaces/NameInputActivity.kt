package com.dicoding.asclepius.interfaces

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityNameInputBinding

class NameInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNameInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNameInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val name = binding.edName.text.toString()
            val intent = Intent(this@NameInputActivity, MainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}