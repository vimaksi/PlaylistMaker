package com.practicum.playlistmaker.favoritetracks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.favoritetracks.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    //добавление в избранное(like)
    @Insert(entity = TrackEntity::class,onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    //получить весь список избранного для отображения в медиатеке
    @Query("SELECT * FROM tracks_table ORDER BY id DESC")
    suspend fun getAllTracks(): List<TrackEntity>
    //удаление определенного трека из избранного ( unlike)
     @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
     suspend fun deleteTrack(trackId: Int)
    //метод для определения наличия трека в избранном
    @Query("SELECT trackId FROM tracks_table WHERE trackId = :trackId")
    suspend fun getTrackId(trackId: Int): Int
    @Transaction
    suspend fun deleteAndInsertTrack(track: TrackEntity) {
        deleteTrack(track.trackId)
        insertTrack(track)}
    @Query("SELECT trackId FROM tracks_table")
    suspend fun getTrackIds(): List<Int>
}