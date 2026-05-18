package com.practicum.playlistmaker.playlist.domain.db

import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.playlist.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist,track: Track)
}