package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.player.domain.api.TracksInteractor
import com.practicum.playlistmaker.player.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(t: Track)
    fun clearHistory()
    interface HistoryConsumer {
        fun consume(searchHistory: List<Track>?)
    }
}