package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.ui.models.TracksState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {
    private val viewModel: TracksViewModel by viewModel()
    private lateinit var simpleTextWatcher: TextWatcher
    val adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private var constTextEdit: String = TEXT_EDIT_VALUE
    private var constIsClearButtonVisible: Int = 8
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.observeState()?.observe(viewLifecycleOwner) {
            render(it)
        }

        if (savedInstanceState != null) {
            constTextEdit = savedInstanceState.getString(EDIT_TEXT, TEXT_EDIT_VALUE)
            constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON, 0)
        }
        binding.inputEditText.setText(constTextEdit.toString())
        binding.clearButton.visibility = constIsClearButtonVisible

        binding.trackList.adapter = adapter
        binding.trackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter.onTrackClick = { track ->
            viewModel?.onCLickTrack(track)
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track))
        }

        binding.trackListSearch.adapter = historyAdapter
        historyAdapter.onTrackClick = { track ->
            viewModel?.onCLickTrack(track)
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track))
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel?.searchDebounce(changedText = s?.toString() ?: "")
                constTextEdit = binding.inputEditText.text.toString()
                binding.clearButton.visibility = clearButtonVisibility(s)
                constIsClearButtonVisible = binding.clearButton.visibility

                if (binding.inputEditText.hasFocus() && s?.isEmpty() == true) {
                    viewModel?.loadHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.updateButton.setOnClickListener {
            viewModel?.searchDebounce(binding.inputEditText.text.toString(), true)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel?.clearHistory()
        }
        binding.clearButton.setOnClickListener {
            binding.inputEditText.setText("")
            closeKeyboard()
        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel?.searchDebounce(binding.inputEditText.text.toString())
        }
    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun showContent(foundTrack: List<Track>) {
        binding.apply {
            errorNoInternet.visibility = View.GONE
            errorNoData.visibility = View.GONE
            progressBar.visibility = View.GONE
            trackList.visibility = View.VISIBLE
            historyLayout.visibility = View.GONE
            closeKeyboard()
            adapter.tracks = foundTrack
            adapter.notifyDataSetChanged()
        }
    }

    fun showHistory(foundTrack: List<Track>) {
        binding.apply {
            errorNoInternet.visibility = View.GONE
            errorNoData.visibility = View.GONE
            progressBar.visibility = View.GONE
            trackList.visibility = View.GONE
            if (foundTrack.isEmpty()) {
                historyLayout.visibility = View.GONE
            } else {
                historyLayout.visibility = View.VISIBLE
                historyAdapter.tracks = foundTrack
            }
            historyAdapter.notifyDataSetChanged()
        }
    }

    fun showLoading() {
        binding.apply {
            errorNoInternet.visibility = View.GONE
            errorNoData.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            trackList.visibility = View.GONE
            historyLayout.visibility = View.GONE
            closeKeyboard()
        }
    }

    fun showError() {
        binding.apply {
            errorNoInternet.visibility = View.VISIBLE
            errorNoData.visibility = View.GONE
            trackList.visibility = View.GONE
            progressBar.visibility = View.GONE
            closeKeyboard()
        }
    }

    fun showEmpty() {
        binding.apply {
            errorNoInternet.visibility = View.GONE
            errorNoData.visibility = View.VISIBLE
            trackList.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun closeKeyboard() {
        requireActivity().currentFocus?.let { view ->
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {//???вью модел и так сохраняет
        //сохранение данных при повороте экрана
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, constTextEdit)
        outState.putInt(
            IS_VISIBLE_BUTTON,
            constIsClearButtonVisible
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }//??
    }

    fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Error -> showError()
            is TracksState.Empty -> showEmpty()
            is TracksState.HistoryContent -> showHistory(state.tracks)
            is TracksState.Content -> showContent(state.tracks)
        }
    }

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val TEXT_EDIT_VALUE = ""
        private const val IS_VISIBLE_BUTTON = "IS_VISIBLE_BUTTON"
    }
}
