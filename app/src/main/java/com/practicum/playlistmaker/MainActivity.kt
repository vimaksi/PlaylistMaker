package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val search = findViewById<TextView>(R.id.search)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на картинку 1!", Toast.LENGTH_SHORT).show()
            }
        }

        search.setOnClickListener(imageClickListener)

        val library = findViewById<TextView>(R.id.library)
        val settings = findViewById<TextView>(R.id.settings)

        library.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на картинку 2!", Toast.LENGTH_SHORT).show()
        }
        settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на картинку 3!", Toast.LENGTH_SHORT).show()
        }
    }
}