package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.api.TracksInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.player.ui.AudioPlayer
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor>{
        TracksInteractorImpl(get())
    }
    single<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }
    single<SharingInteractor>{
        SharingInteractorImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}