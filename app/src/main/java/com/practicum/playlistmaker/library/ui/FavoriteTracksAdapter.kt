package com.practicum.playlistmaker.library.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.player.domain.models.Track

class FavoriteTracksAdapter(val clickListener: TrackClickListener) :
    RecyclerView.Adapter<FavoriteTracksViewHolder>() {
    var tracks = listOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTracksViewHolder =
        FavoriteTracksViewHolder.Companion.from(parent)

    override fun onBindViewHolder(
        holder: FavoriteTracksViewHolder,
        position: Int
    ) {
        holder.bind(tracks.get(position))
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks.get(position)) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}