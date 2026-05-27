package com.practicum.playlistmaker.playlist.domain.db

import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun insertTrackInPlaylist(playlist: Playlist, track: Track)
    fun getPlaylist(playlistId: Int): Flow<Playlist>
    fun getTrackInPlaylist(trackListId: List<Int>): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun sharePlaylist(playlist: Playlist, tracks: List<Track>)
    suspend fun updatePlaylist(playlist: Playlist)
}