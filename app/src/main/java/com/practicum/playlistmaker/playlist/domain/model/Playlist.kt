package com.practicum.playlistmaker.playlist.domain.model

import com.practicum.playlistmaker.player.domain.models.Track

class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String,
    val image: String,
    var tracks: MutableList<Int>,
    var count: Int
)
//класс для слоёв Domain и Presentation