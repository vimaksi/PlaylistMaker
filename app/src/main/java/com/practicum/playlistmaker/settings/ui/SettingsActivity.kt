package com.practicum.playlistmaker.settings.ui


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.TracksApplication
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        //подпись на отслеживание состояния темы

        val main = findViewById<Toolbar>(R.id.back_to_main)

        main.setOnClickListener {
            finish()
        }

        val message = findViewById<TextView>(R.id.share_app)
        message.setOnClickListener {
            viewModel.shareLink()
            //Поделиться приложением — отправить ссылку на приложение в систему.

        }

        val send = findViewById<TextView>(R.id.write_support)
        send.setOnClickListener {
            viewModel.openEmail()
            //Написать в поддержку — открыть почтовый клиент с адресом поддержки.
        }
        val browse = findViewById<TextView>(R.id.user_agreement)
        browse.setOnClickListener {
            viewModel.openTerms()
            //Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.observeState().observe(this) {state ->
            if (themeSwitcher.isChecked != state.isDarkTheme) {
                themeSwitcher.isChecked = state.isDarkTheme
            }
        }

        themeSwitcher.isChecked = viewModel.getTheme()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.changeTheme(checked)
            (application as TracksApplication).switchTheme(checked)
        }
    }
}