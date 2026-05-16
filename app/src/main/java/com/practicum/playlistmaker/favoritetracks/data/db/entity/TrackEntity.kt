package com.practicum.playlistmaker.favoritetracks.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long,
    val artworkUrl100: String,   // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?, //Год релиза трека
    val primaryGenreName: String, //Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String //ссылка на трек
)