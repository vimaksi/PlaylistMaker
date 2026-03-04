package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    //    fun setDarkTheme(enabled: Boolean)
//    fun isDarkThemeEnabled(): Boolean
//    // SettingsInteractor для сохранения/получения темы.
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)

}