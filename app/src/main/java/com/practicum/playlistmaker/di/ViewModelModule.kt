package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.library.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker.library.presentation.PlaylistsViewModel
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.search.presentation.TracksViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { TracksViewModel(get(), get()) }
    factory { MediaPlayer() }
    viewModel { (track: Track) -> AudioPlayerViewModel(track, get(), get()) }
    viewModel { PlaylistsViewModel() }
    viewModel { FavoriteTracksViewModel(get()) }
}