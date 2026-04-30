package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.domain.api.SearchTracksRepository
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient, private val appDatabase: AppDatabase) : SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TrackSearchResponse) {
                    val data = response.results.map{ trackDto ->
                        Track(
                            trackDto.trackId,
                            trackDto.trackName,
                            trackDto.artistName,
                            trackDto.trackTimeMillis,
                            trackDto.artworkUrl100,
                            trackDto.collectionName,
                            trackDto.releaseDate,
                            trackDto.primaryGenreName,
                            trackDto.country,
                            trackDto.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
