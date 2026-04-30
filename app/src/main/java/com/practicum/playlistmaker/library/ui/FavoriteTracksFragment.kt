package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.library.presentation.FavoriteTracksState
import com.practicum.playlistmaker.library.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.root.ui.RootActivity
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteTracksBinding
    private val viewModel: FavoriteTracksViewModel by viewModel()
    private lateinit var onTrackSearchDebounce: (Track) -> Unit
    private var adapter: FavoriteTracksAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteTracksAdapter { track ->
            (activity as RootActivity).animateBottomNavigationView()
            onTrackSearchDebounce(track)
        }
        binding.trackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = adapter

        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        onTrackSearchDebounce =
            debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                findNavController().navigate(
                    R.id.action_libraryFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(track)
                )
            }
    }
    fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showContent(state.tracks)
            is FavoriteTracksState.Empty -> showEmpty()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding.trackList.adapter = null
    }
    private fun showEmpty() {
        binding.trackList.visibility = View.GONE
        binding.emptyLibrary.visibility = View.VISIBLE
    }

    private fun showContent(foundTrack: List<Track>) {
        binding.trackList.visibility = View.VISIBLE
        binding.emptyLibrary.visibility = View.GONE
        adapter?.tracks = foundTrack
        adapter?.notifyDataSetChanged()
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoriteTracksFragment()
    }
}