package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.player.presentation.PlaylistPlayerState
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.PlaylistAdapter
import com.practicum.playlistmaker.root.ui.RootActivity
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class AudioPlayerFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding
    private var adapter: PlaylistPlayerAdapter? = null

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

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlaylist(it)
        }

        binding.overlay.visibility = View.GONE

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.observeTrackInPlaylistState().observe(viewLifecycleOwner) {
            if (it.inPlaylist) {
                Toast.makeText(
                    requireContext(),
                    "Трек уже добавлен в плейлист ${it.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "Добавлено в плейлист ${it.name}",
                    Toast.LENGTH_SHORT
                ).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        binding.addToPl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.fillPlaylist()
        }
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_createPlaylistFragment
            )
        }
        adapter = PlaylistPlayerAdapter { playlist ->
            viewModel.addTrackToPlaylist(playlist, track)
        }

        binding.playlist.adapter = adapter
        binding.playlist.layoutManager = GridLayoutManager(
            requireContext(), /*Количество столбцов*/
            1
        ) //ориентация по умолчанию — вертикальная



        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
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

    fun renderPlaylist(state: PlaylistPlayerState) {
        when (state) {
            is PlaylistPlayerState.Content -> showContentPlaylist(state.playlists)
            is PlaylistPlayerState.Empty -> showEmptyPlaylist()
        }
    }

    private fun showContentPlaylist(playlists: List<Playlist>) {
        binding.playlist.visibility = View.VISIBLE
        adapter?.playlists = playlists
        adapter?.notifyDataSetChanged()
    }

    private fun showEmptyPlaylist() {
        binding.playlist.visibility = View.GONE
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

//Если пользователь находится на экране «Аудиоплеер» и видит всплывающее
//окно добавления трека в плейлист, то при нажатии на кнопку «Новый плейлист» окно добавления трека
//в плейлист исчезает и пользователь перенаправляется на экран «Создание плейлиста».
