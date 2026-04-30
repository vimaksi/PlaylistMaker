package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    //добавление в избранное(like)
    @Insert(entity = TrackEntity::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    //получить весь список избранного для отображения в медиатеке
    @Query("SELECT * FROM tracks_table ORDER BY id DESC")
    suspend fun getAllTracks(): List<TrackEntity>
    //удаление определенного трека из избранного ( unlike)
     @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
     suspend fun deleteTrack(trackId: Int)
    //метод для определения наличия трека в избранном
    @Query("SELECT trackId FROM tracks_table ")
    suspend fun getTrackIds(): List<Int>
    @Transaction
    suspend fun deleteAndInsertTrack(track: TrackEntity) {
        deleteTrack(track.trackId)
        insertTrack(track)}
}
