package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
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

        val message = findViewById<TextView>(R.id.share_app)
        message.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.link_android_developer))
            startActivity(Intent.createChooser(shareIntent,"Отправить через"))
        }

        val send = findViewById<TextView>(R.id.write_support)
        send.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.recipient)))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string .letters_theme))
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.letter_text))
            startActivity(sendIntent)
        }
        val browse = findViewById<TextView>(R.id.user_agreement)
        browse.setOnClickListener {
            val uri = Uri.parse(getString(R.string.agreement))
            val browseIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browseIntent)
        }
    }
}