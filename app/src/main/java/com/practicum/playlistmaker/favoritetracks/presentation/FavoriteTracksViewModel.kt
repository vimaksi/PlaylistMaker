package com.practicum.playlistmaker.favoritetracks.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favoritetracks.domain.db.LikeInteractor
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val likeInteractor: LikeInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            likeInteractor.likeTracks().collect { tracks -> processResult(tracks) }
        }
    }

    fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(
                FavoriteTracksState.Empty("")
            )
        } else {
            renderState(FavoriteTracksState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteTracksState) {
        stateLiveData.postValue(state)
    }
}