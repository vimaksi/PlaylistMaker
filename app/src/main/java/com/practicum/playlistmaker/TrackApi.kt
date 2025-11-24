package com.practicum.playlistmaker
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface TrackApi {
    @GET("search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}