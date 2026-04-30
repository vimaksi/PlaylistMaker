package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.domain.db.LikeInteractor
import com.practicum.playlistmaker.library.domain.impl.LikeInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }
    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    factory<LikeInteractor> {
        LikeInteractorImpl(get())
    }
}