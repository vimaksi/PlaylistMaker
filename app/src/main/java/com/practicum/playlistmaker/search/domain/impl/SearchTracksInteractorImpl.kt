package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksInteractorImpl(private val repository: SearchTracksRepository) :
    SearchTracksInteractor {
    override fun searchTracks(
        expression: String
    ): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}