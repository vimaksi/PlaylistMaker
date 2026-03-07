package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.player.domain.models.Track

interface SearchHistoryRepository {
    fun saveToHistory(t: Track)
    fun getHistory(): Resource<List<Track>>
    fun clearHistory()
}