package com.practicum.playlistmaker.data.dto

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import java.util.Collections.emptyList

private const val MAX_SIZE = 10

class SearchHistoryStorage(val sharedPreferences: SharedPreferences) {
    fun getHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_TRACK_HISTORY_KEY, null)
        if (json == null) return emptyList()
        return createHistoryFromJson(json)
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_TRACK_HISTORY_KEY).apply()
    }

    fun addTrackToHistory(track: Track) {
        val list = getHistory().toMutableList()

        list.removeAll { track.trackId == it.trackId }
        if (list.size >= MAX_SIZE) {
            list.removeAt(list.lastIndex)
        }
        list.add(0, track)
        sharedPreferences.edit()
            .putString(SEARCH_TRACK_HISTORY_KEY, createJsonFromHistory(list))
            .apply()
    }

    private fun createJsonFromHistory(history: List<Track>): String {
        return Gson().toJson(history)
    }

    private fun createHistoryFromJson(json: String): MutableList<Track> {
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    companion object {
        const val SEARCH_TRACK_HISTORY_KEY = "searchTrackHistory"
    }
}