package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.player.domain.api.TracksRepository
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient,private val appDatabase: AppDatabase) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TrackSearchResponse) {
                   val listLike =  appDatabase.trackDao().getTrackIds()
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
                            trackDto.previewUrl,
                          isLike = listLike.contains(trackDto.trackId)
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
