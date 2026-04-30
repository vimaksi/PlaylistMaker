package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.TracksInteractor
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class TracksViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    private var latestSearchText: String? = null
    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changeText ->
            search(changeText)
        }

    fun searchDebounce(changedText: String, force: Boolean = false) {
        if (!force && latestSearchText == changedText) return
        latestSearchText = changedText
        trackSearchDebounce(changedText)
    }

    private fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText)
                    .collect { pair -> processResult(pair.first, pair.second) }
            }
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        loadHistory()
    }

    fun saveTrackToHistory(track: Track) {
        searchHistoryInteractor.saveToHistory(track)
    }

    fun loadHistory() {
        searchHistoryInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {//???
            override fun consume(searchHistory: List<Track>?) {
                val tracksHistory = mutableListOf<Track>()
                tracksHistory.clear()
                tracksHistory.addAll(searchHistory ?: emptyList())
                renderState(
                    TracksState.HistoryContent(
                        tracks = searchHistory ?: emptyList()
                    )
                )
            }
        })
    }

    fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.clear()
            tracks.addAll(foundTracks)
        }
        when {
            (foundTracks?.isEmpty() == true) -> {
                renderState(
                    TracksState.Empty("")
                )
            }

            (errorMessage != null) -> {
                renderState(
                    TracksState.Error("")
                )
            }

            else -> {
                renderState(
                    TracksState.Content(
                        tracks = tracks,
                    )
                )
            }
        }

    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}