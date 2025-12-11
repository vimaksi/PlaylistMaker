package com.practicum.playlistmaker


import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"

class TrackAdapter () : RecyclerView.Adapter<TrackViewHolder>() {
    var onTrackClick: ((Track) -> Unit)? = null
    var tracks = mutableListOf<Track>()
    //val searchHistory = SearchHistory()
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