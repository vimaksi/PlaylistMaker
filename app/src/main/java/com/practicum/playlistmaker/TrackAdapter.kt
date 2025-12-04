package com.practicum.playlistmaker


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//class TrackAdapter (private val trackList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {
class TrackAdapter () : RecyclerView.Adapter<TrackViewHolder>() {
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
        holder.itemView.setOnClickListener {  }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}