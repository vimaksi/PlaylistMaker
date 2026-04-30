package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.domain.db.LikeInteractor
import com.practicum.playlistmaker.library.domain.db.LikeRepository
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class LikeInteractorImpl(private val likeRepository: LikeRepository): LikeInteractor {
    override fun likeTracks(): Flow<List<Track>> {
       return likeRepository.likeTracks()
    }

    override suspend fun unlikeTrack(track: Track) {
        likeRepository.unlikeTrack(track)
    }

    override suspend fun likeTrack(track: Track) {
        likeRepository.likeTrack(track)
    }
}