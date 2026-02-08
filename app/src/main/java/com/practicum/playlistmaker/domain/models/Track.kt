package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track (
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long,
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?, //Год релиза трека
    val primaryGenreName: String, //Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String //ссылка на трек
) : Parcelable
{
   fun getFormattedTime(): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)//???

   fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")///???

}