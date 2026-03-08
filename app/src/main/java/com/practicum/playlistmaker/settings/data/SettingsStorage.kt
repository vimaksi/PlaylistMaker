package com.practicum.playlistmaker.settings.data

interface SettingsStorage {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkTheme(darkTheme: Boolean)
}