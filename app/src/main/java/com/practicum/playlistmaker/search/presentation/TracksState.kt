package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.player.domain.models.Track

sealed interface TracksState {
    object Loading : TracksState
    data class Content(
        val tracks: List<Track>,
    ) : TracksState

    data class HistoryContent(
        val tracks: List<Track>
    ) : TracksState

    data class Error(val errorMessage: String) : TracksState
    data class Empty(val isEmpty: String) : TracksState
}