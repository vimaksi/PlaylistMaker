package com.practicum.playlistmaker.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class TracksApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TracksApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

//        val darkTheme = Creator.provideSettingsInteractor(this).getThemeSettings().isDarkTheme

        val themeInteractor: SettingsInteractor = getKoin().get()

        switchTheme(themeInteractor.getThemeSettings().isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}