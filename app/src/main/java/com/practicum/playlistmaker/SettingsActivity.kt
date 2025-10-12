package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val main = findViewById<Toolbar>(R.id.back_to_main)

        main.setOnClickListener{
            finish()
        }
    }
}