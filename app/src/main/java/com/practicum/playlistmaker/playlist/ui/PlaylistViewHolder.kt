package com.practicum.playlistmaker.playlist.ui


import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.model.Playlist

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = itemView.findViewById(R.id.name)
    private val count: TextView = itemView.findViewById(R.id.count)
    private val image: ImageView = itemView.findViewById(R.id.image)
    fun bind(model: Playlist) {
        name.text = model.name
        val traсks =
            when (model.count) {
                1 -> "трек"
                in 2..4 -> "трека"
                in 5..9, 0 -> "треков"
                else -> ""
            }
        count.text = "${model.count.toString()} $traсks"

        Glide.with(itemView)
            .load(model.image)
            .placeholder(R.drawable.ic_placeholder_103)
            .centerCrop()
            .transform(CenterCrop(),RoundedCorners(dpToPx(8f, itemView.context)))
            .into(image)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}