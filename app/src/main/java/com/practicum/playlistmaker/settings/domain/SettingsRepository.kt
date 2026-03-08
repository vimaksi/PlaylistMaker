package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}

//В целом так и есть, когда основная задача приложения — просто показывать данные с сервера.
//В таких случаях, когда не хочется иметь SettingsInteractor без логики, можно использовать во ViewModel сразу SettingsRepository.
//Интерфейс SettingsRepository относится к слою Domain, поэтому в целом мы не нарушаем Clean Architecture.
//
//
//Но, как вы могли заметить, не всегда они будут независимы друг от друга.
//Вспомним экран «Настройки», на котором есть переход в настройки и во внешние приложения. Получается, пакет settings зависит от sharing.
//В таких случаях мы рекомендуем ограничить использование классов одной возможности слоем Domain в другой.
//Значит, в пакете settings могут использоваться лишь классы/интерфейсы, находящиеся в пакете sharing.domain, — Interactor и модели, нужные для работы с ним.