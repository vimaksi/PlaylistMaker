package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter () : RecyclerView.Adapter<TrackViewHolder>() {
    var onTrackClick: ((Track) -> Unit)? = null
    var tracks = mutableListOf<Track>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
      return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(tracks.get(position))
        holder.itemView.setOnClickListener {
            onTrackClick?.invoke(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}