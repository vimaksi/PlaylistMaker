package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(TrackApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = trackService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply {
                resultCode = resp.code()
            }
        } else {
            return Response().apply { resultCode = 400 }//неуспех
        }
    }
    companion object{
        const val trackBaseUrl = "https://itunes.apple.com/"
    }
}