package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.player.domain.models.Track

class TrackViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
//    private val sourceImage: ImageView = itemView.findViewById(R.id.imageAlbum)
//    private val sourceName: TextView = itemView.findViewById(R.id.trackName)
//    private val sourceTime: TextView = itemView.findViewById(R.id.trackTime)
//    private val sourceArtist: TextView = itemView.findViewById(R.id.artistName)

    fun bind(model: Track) {
        binding.trackName.text = model.trackName
        binding.artistName.text = ""
        binding.artistName.text = model.artistName
        binding.trackTime.text = model.getFormattedTime()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.imageAlbum)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackViewBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}