package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.player.domain.models.Track

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>
) : SearchHistoryRepository {

    override fun saveToHistory(t: Track) {
        val tracks = storage.getData() ?: arrayListOf()
        tracks.removeAll { t.trackId == it.trackId }
        if (tracks.size >= MAX_SIZE) {
            tracks.removeAt(tracks.lastIndex)
        }
        tracks.add(0, t)
        storage.storeData(tracks)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData() ?: listOf()
        return Resource.Success(tracks)
    }

    override fun clearHistory() {
        storage.clearData()
    }

    companion object {
        private const val MAX_SIZE = 10
    }
}