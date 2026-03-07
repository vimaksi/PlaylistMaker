package com.practicum.playlistmaker.settings.data.impl

import com.practicum.playlistmaker.settings.data.storage.SettingsStorage
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(val settingsStorage: SettingsStorage) : SettingsRepository {
    //    override fun saveDarkTheme(enabled: Boolean) {
//        settingsStorage.setDarkTheme(enabled)
//    }
//
//    override fun isDarkThemeEnabled(): Boolean {
//        return settingsStorage.isDarkThemeEnabled()
//    }
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(settingsStorage.isDarkThemeEnabled())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsStorage.setDarkTheme(settings.isDarkTheme)
    }
}