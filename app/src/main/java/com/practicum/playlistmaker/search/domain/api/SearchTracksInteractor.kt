package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksInteractor {
    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>
}