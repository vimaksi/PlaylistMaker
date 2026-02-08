package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): MutableList<Track>
    fun clearHistory()
    fun addTrackToHistory(track: Track)
}