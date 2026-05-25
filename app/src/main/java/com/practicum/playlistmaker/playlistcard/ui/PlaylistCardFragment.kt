package com.practicum.playlistmaker.playlistcard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createplaylist.ui.EditPlaylistFragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistCardBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.presentation.PlaylistPlayerState
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlistcard.presentation.PlaylistCardState
import com.practicum.playlistmaker.playlistcard.presentation.PlaylistCardViewModel
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlaylistCardFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistCardBinding
    private val viewModel: PlaylistCardViewModel by viewModel() { parametersOf(playlistId) }
    val playlistId: Int by lazy {
        requireArguments().getInt(ARGS_PLAYLIST_ID, -1).takeIf { it != -1 }
            ?: error("Playlist ID is missing")
    }
    private lateinit var onTrackSearchDebounce: (Track) -> Unit
    private var adapter: TrackInPlaylistAdapter? = null
    private var adapterPlaylist: PlaylistCardAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePlaylistCardState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.menuButton.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        onTrackSearchDebounce =
            debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                findNavController().navigate(
                    R.id.action_playlistCardFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(track)
                )
            }
        adapter = TrackInPlaylistAdapter(
            clickListener = { track -> onTrackSearchDebounce(track) },
            longClickListener = { track -> showDialog(track) })
        binding.trackList.adapter = adapter
        binding.trackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapterPlaylist = PlaylistCardAdapter()
        binding.playlist.adapter = adapterPlaylist
        binding.playlist.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.overlay.visibility = View.GONE
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.share.setOnClickListener {
            sharePlaylist()
        }
        binding.shareTextView.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }
        binding.menu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        binding.tracksBottomSheet.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        binding.tracksBottomSheet.visibility = View.GONE

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.deleteTextView.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            MaterialAlertDialogBuilder(requireContext())
               // .setTitle("${getString(R.string.delete_playlist_header)} ${binding.name.text}?")
                .setTitle("${getString(R.string.delete_playlist_header)}")
                .setMessage("${getString(R.string.delete_pl_descr)}")
                .setNeutralButton("${getString(R.string.cancel)}") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.delete_mes)) { dialog, which ->
                     viewModel.deletePlaylist()
                }
                .show()
       }
        binding.editTextView.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistCardFragment_to_editPlaylistFragment
            , EditPlaylistFragment.createArgs( adapterPlaylist!!.playlists[0]))
        }
    }
    private fun showDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.would_you_delete_track))
            .setNegativeButton(getString(R.string.no_message)) { dialog, which ->
            }
            .setPositiveButton((R.string.yes_message)) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track)
            }
            .show()
    }

    private fun render(state: PlaylistCardState) {
        when (state) {
            is PlaylistCardState.Content -> {
                showContent(state.playlist, state.tracks)
            }

            else -> findNavController().popBackStack()
        }
    }

    private fun showContent(playlist: Playlist, tracks: List<Track>) {
        binding.apply {
            name.text = playlist.name
            description.text = playlist.description

            Glide.with(requireContext())
                .load(playlist.image)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_360)
                //.transform(CenterCrop(),RoundedCorners(dpToPx(8f, itemView.context)))
                .into(image)

            val traсksDescr =
                when (playlist.count) {
                    1 -> "трек"
                    in 2..4 -> "трека"
                    in 5..9, 0 -> "треков"
                    else -> ""
                }
            count.text = "${playlist.count.toString()} $traсksDescr"

            val traсksTime = SimpleDateFormat(
                "mm",
                Locale.getDefault()
            ).format(tracks.sumOf { it.trackTimeMillis }).toInt()

            val tracksMin =
                when (traсksTime) {
                    1 -> "минута"
                    in 2..4 -> "минуты"
                    else -> "минут"
                }
            time.text = "${traсksTime.toString()} $tracksMin"

            if (tracks.isEmpty())
            {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_playlist_mes),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        adapter?.tracks = tracks
        adapter?.notifyDataSetChanged()

        adapterPlaylist?.playlists = listOf(playlist)
        adapterPlaylist?.notifyDataSetChanged()
    }
    private fun sharePlaylist () {
        if (adapter!!.tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.empty_playlist_card),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            viewModel.sharePlaylist()
        }
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        private const val ARGS_PLAYLIST_ID = "PLAYLIST_EXTRA"
        fun createArgs(playlistId: Int): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)

    }
}
//Список треков отсортирован по убыванию добавления в
//плейлист: последние добавленные треки находятся в верхней части списка.