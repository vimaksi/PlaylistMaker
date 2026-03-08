package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.StorageClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.TrackApiService
import com.practicum.playlistmaker.search.data.storage.PrefsStorageClient
import com.practicum.playlistmaker.settings.data.SettingsStorage
import com.practicum.playlistmaker.settings.data.storage.SettingsStorageImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.data.storage.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<TrackApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApiService::class.java)
    }
    single {
        androidContext()
            .getSharedPreferences(
                "TRACKS_SEARCH",
                Context.MODE_PRIVATE
            )
    }
    factory { Gson() }

    single<StorageClient<ArrayList<Track>>> {
        PrefsStorageClient(
            "HISTORY",
            object : TypeToken<ArrayList<Track>>() {}.type,
            get(),
            get()
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single <ExternalNavigator>{
        ExternalNavigatorImpl(androidContext())
    }

    single<SettingsStorage> {
        SettingsStorageImpl(
            androidContext().getSharedPreferences(
                "playlistmaker",
                Context.MODE_PRIVATE
            )
        )
    }
}

