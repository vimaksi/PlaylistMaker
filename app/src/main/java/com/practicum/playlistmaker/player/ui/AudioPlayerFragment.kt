package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class AudioPlayerFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding

    val track: Track by lazy {
        requireArguments().getParcelable<Track>(ARGS_TRACK_ID)
            ?: error("Track is missing")
    }

    private val viewModel: AudioPlayerViewModel by viewModel { parametersOf(track) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(dpToPx(8f)))
            .into(binding.imageAlbum)

        binding.apply {
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            trackTime.text = track.getFormattedTime()
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country
        }
        visibleText(track)

        binding.play.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        binding.pause.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        binding.menuButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.like.setOnClickListener { viewModel.onLikeClicked() }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            changeButton(it.isPlayButtonEnabled)
            binding.trackTimeCurrent.text = it.progress
        }

        viewModel.observeLikeState().observe(viewLifecycleOwner) {
            if (it.isLike) {
                binding.like.setImageResource(R.drawable.ic_like_active_25_23)
            } else {
                binding.like.setImageResource(R.drawable.ic_like_25_23)
            }
        }
    }

    private fun visibleText(track: Track) {
        binding.apply {
            trackYear.visibility = View.GONE
            year.visibility = View.GONE
            trackAlbum.visibility = View.GONE
            album.visibility = View.GONE

            if (!track.releaseDate.isNullOrEmpty()) {
                trackYear.text = track.releaseDate?.take(4)
                trackYear.visibility = View.VISIBLE
                year.visibility = View.VISIBLE
            }

            if (!track.collectionName.isNullOrEmpty()) {
                trackAlbum.text = track.collectionName
                trackAlbum.visibility = View.VISIBLE
                album.visibility = View.VISIBLE
            }
        }
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }


    private fun changeButton(isPlayButtonEnabled: Boolean) {
        if (isPlayButtonEnabled) {
            binding.play.visibility = View.VISIBLE
            binding.pause.visibility = View.GONE
        } else {
            binding.play.visibility = View.GONE
            binding.pause.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        private const val ARGS_TRACK_ID = "TRACK_EXTRA"
        fun createArgs(track: Track): Bundle = bundleOf(ARGS_TRACK_ID to track)
    }
}
