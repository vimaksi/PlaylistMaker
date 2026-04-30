package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}