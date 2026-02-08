package com.practicum.playlistmaker.domain

import android.content.Context
import com.practicum.playlistmaker.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.TrackRepositoryImpl
import com.practicum.playlistmaker.data.dto.SettingsStorage
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val prefs = context.getSharedPreferences("playlistmaker", Context.MODE_PRIVATE)
        val storage = SettingsStorage(prefs)
        val repository = SettingsRepositoryImpl(storage)
        return SettingsInteractorImpl(repository)
    }
}