package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.favoritetracks.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker.favoritetracks.ui.FavoriteTracksAdapter
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.presentation.PlaylistState
import com.practicum.playlistmaker.playlist.presentation.PlaylistViewModel
import com.practicum.playlistmaker.root.ui.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: PlaylistViewModel by viewModel()
    private var adapter: PlaylistAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_createPlaylistFragment
            )
        }

        adapter = PlaylistAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), /*Количество столбцов*/
            2
        ) //ориентация по умолчанию — вертикальная
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding.recyclerView.adapter = null
    }

    fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlists)
            is PlaylistState.Empty -> showEmpty()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.newPlaylist.visibility = View.VISIBLE
        binding.emptyLibrary.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        adapter?.playlists = playlists
        adapter?.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.newPlaylist.visibility = View.VISIBLE
        binding.emptyLibrary.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}