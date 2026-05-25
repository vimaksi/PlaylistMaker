package com.practicum.playlistmaker.playlistcard.presentation

import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.playlist.domain.model.Playlist

sealed interface PlaylistCardState {
    data class Content(val playlist: Playlist, val tracks: List<Track>) : PlaylistCardState
    data class Empty(val isEmpty: String) : PlaylistCardState
}