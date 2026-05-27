package com.practicum.playlistmaker.createplaylist.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.createplaylist.presentation.EditPlaylistViewModel
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createplaylist.presentation.PlaylistInfoState
import java.io.File

class EditPlaylistFragment : CreatePlaylistFragment() {
    private var fileP: File? = null
    val playlist: Playlist by lazy {
        requireArguments().getParcelable<Playlist>(PLAYLIST_ARGS)
            ?: error("Playlist is missing")
    }
    override val viewModel: EditPlaylistViewModel by viewModel { parametersOf(playlist) }

    //по нажатию на кнопку pickImage запускаем photo picker
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //обрабатываем событие выбора пользователем фотографии
            if (uri != null) {
                binding.pickerImage.background = null
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_placeholder_312)
                    .transform(
                        CenterCrop(), RoundedCorners(
                            dpToPx(
                                8f
                            )
                        )
                    )
                    .into(
                        binding.pickerImage
                    )

                val uniqueName = "cover_${System.currentTimeMillis()}"
                fileP = saveImageToPrivateStorage(uri, uniqueName)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menuButton.title = getString(R.string.edit)
        binding.create.text = getString(R.string.save)
        viewModel.fillData()
        viewModel.observePlaylistInfoState().observe(viewLifecycleOwner) {
            renderPlaylist(it)
        }
        binding.menuButton.setOnClickListener { findNavController().popBackStack() }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        binding.pickerImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onSavePlaylist() {
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val imagePath = fileP?.absolutePath ?: playlist.image
        viewModel.createPlaylist(name, description, imagePath)
        findNavController().popBackStack()
    }

    private fun renderPlaylist(state: PlaylistInfoState) {
        when (state) {
            is PlaylistInfoState.Content -> showContent(state.playlist)
            is PlaylistInfoState.Empty -> showEmpty()
        }
    }

    private fun showContent(playlist: Playlist) {
        binding.apply {
            nameEditText.setText(playlist.name)
            descriptionEditText.setText(playlist.description)
            binding.pickerImage.background = null
            val imageSource = if (playlist.image.isEmpty()) {
                R.drawable.ic_placeholder_312
            } else {
                playlist.image
            }

            Glide.with(requireContext())
                .load(imageSource)
                .placeholder(R.drawable.ic_placeholder_312)
                .transform(
                    CenterCrop(), RoundedCorners(
                        dpToPx(
                            8f
                        )
                    )
                )
                .into(pickerImage)
        }
    }

    private fun showEmpty() {
        binding.nameEditText.setText(playlist.name)
        binding.descriptionEditText.setText(playlist.description)
    }

    companion object {
        private const val PLAYLIST_ARGS = "PLAYLIST_EXTRA"
        fun createArgs(playlist: Playlist): Bundle = bundleOf(PLAYLIST_ARGS to playlist)
    }
}