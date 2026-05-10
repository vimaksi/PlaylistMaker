package com.practicum.playlistmaker.library.presentation

import com.practicum.playlistmaker.player.domain.models.Track

sealed interface FavoriteTracksState {
    data class Content(
        val tracks: List<Track>,
    ) : FavoriteTracksState
    data class Empty(val isEmpty: String) : FavoriteTracksState
}