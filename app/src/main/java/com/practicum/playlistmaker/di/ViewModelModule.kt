package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.createplaylist.presentation.CreatePlaylistViewModel
import com.practicum.playlistmaker.createplaylist.presentation.EditPlaylistViewModel
import com.practicum.playlistmaker.favoritetracks.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker.playlist.presentation.PlaylistViewModel
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlistcard.presentation.PlaylistCardViewModel
import com.practicum.playlistmaker.search.presentation.TracksViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { TracksViewModel(get(), get()) }
    factory { MediaPlayer() }
    viewModel { (track: Track) -> AudioPlayerViewModel(track, get(), get(), get()) }
    viewModel { FavoriteTracksViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
    viewModel { CreatePlaylistViewModel(get()) }
    viewModel { (playlistId: Int) -> PlaylistCardViewModel(playlistId, get()) }
    viewModel { (playlist: Playlist) -> EditPlaylistViewModel(get(),playlist) }
}