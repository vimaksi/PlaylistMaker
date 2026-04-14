package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}