package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long,
    //val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
)
{
    fun getFormattedTime(): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}