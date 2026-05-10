package com.practicum.playlistmaker.settings.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    //Поскольку экран настроек сочетает возможности сохранения темы приложения и передачу информации в сторонние приложения,
    //то мы видим в конструкторе SettingsViewModel два интерактора под каждую из этих возможностей:
    //SharingInteractor для передачи информации в сторонние приложения и SettingsInteractor для сохранения/получения темы.
    private val stateLiveData = (
            ThemeSettings(settingsInteractor.getThemeSettings().isDarkTheme))

    fun observeState(): LiveData<ThemeSettings> = MutableLiveData(
        ThemeSettings(settingsInteractor.getThemeSettings().isDarkTheme)
    )

    private val handler = Handler(Looper.getMainLooper())
    fun shareLink() {
        sharingInteractor.shareLink()
        //Поделиться приложением — отправить ссылку на приложение в систему.
    }

    fun openEmail() {
        sharingInteractor.openEmail()
        //Написать в поддержку — открыть почтовый клиент с адресом поддержки.
    }

    fun openTerms() {
        sharingInteractor.openTerms()
        //Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
    }

    fun changeTheme(b: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(b))
    }

    fun getTheme(): Boolean {
        return settingsInteractor.getThemeSettings().isDarkTheme
    }


    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SETTINGS_REQUEST_TOKEN)
    }

    companion object {
        private val SETTINGS_REQUEST_TOKEN = Any()
//        fun getFactory(
//            sharingInteractor: SharingInteractor,
//            settingsInteractor: SettingsInteractor
//        ): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                SettingsViewModel(sharingInteractor, settingsInteractor)
//            }
//        }
    }
}