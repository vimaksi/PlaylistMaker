package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favoritetracks.data.LikeRepositoryImpl
import com.practicum.playlistmaker.favoritetracks.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.favoritetracks.domain.db.LikeRepository
import com.practicum.playlistmaker.playlist.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.data.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.playlist.domain.db.PlaylistRepository
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.SearchTrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.SharingRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<SearchTracksRepository> {
        SearchTrackRepositoryImpl(get(), get())
    }
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
    factory<SharingRepository> {
        SharingRepositoryImpl(get(), get())
    }
    factory<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    factory { TrackDbConvertor() }
    single<LikeRepository> {
        LikeRepositoryImpl(get(), get())
    }
    factory {
        PlaylistDbConvertor()
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(),get())
    }
}