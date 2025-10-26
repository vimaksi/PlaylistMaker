package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContentView(R.layout.activity_settings)

        val main = findViewById<Toolbar>(R.id.back_to_main)

        main.setOnClickListener{
            finish()
        }
        //отправить сообщение
        //val shareIntent = Intent(Intent.ACTION_SEND)
        //shareIntent.putExtra(R.string.link_android_developer)


        val send = findViewById<TextView>(R.id.write_support)
        send.setOnClickListener {
            //отправить емеил
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("mailto:")
            //получатель
            sendIntent.putExtra(Intent.EXTRA_EMAIL, R.string.recipient)
            //sendIntent.putExtra(Intent.EXTRA_)
            sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.letter_text)
            this.startActivity(sendIntent)
            //тема?
            //текст?
        }
        val browse = findViewById<TextView>(R.id.user_agreement)
        browse.setOnClickListener {
            val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
        }
    }
}