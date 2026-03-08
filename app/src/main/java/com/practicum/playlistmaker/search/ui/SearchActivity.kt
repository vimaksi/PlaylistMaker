package com.practicum.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.AudioPlayer
import com.practicum.playlistmaker.search.ui.models.TracksState
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private val viewModel:TracksViewModel by viewModel()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var simpleTextWatcher: TextWatcher
    val adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private var constTextEdit: String = TEXT_EDIT_VALUE
    private var constIsClearButtonVisible: Int = 8

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this, TracksViewModel.getFactory())
//            .get(TracksViewModel::class.java)

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        if (savedInstanceState != null) {
            constTextEdit = savedInstanceState.getString(EDIT_TEXT, TEXT_EDIT_VALUE)
            constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON, 0)
        }
        binding.inputEditText.setText(constTextEdit.toString())
        binding.clearButton.visibility = constIsClearButtonVisible

        binding.back.setOnClickListener {
            finish()
        }

        binding.trackList.adapter = adapter
        binding.trackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.onTrackClick = { track ->
            viewModel?.onCLickTrack(track)
                startAudioPlayerActivity(track)
            }

        binding.trackListSearch.adapter = historyAdapter
        historyAdapter.onTrackClick = { track ->
            viewModel?.onCLickTrack(track)
            startAudioPlayerActivity(track)
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
        this.currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //сохранение данных при повороте экрана
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, constTextEdit)
        outState.putInt(
            IS_VISIBLE_BUTTON,
            constIsClearButtonVisible
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        constTextEdit = savedInstanceState.getString(
            EDIT_TEXT,
            TEXT_EDIT_VALUE
        )
        constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON, 0)
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

    fun startAudioPlayerActivity(track: Track) {
        val audioPlayerIntent = Intent(this, AudioPlayer::class.java)
        audioPlayerIntent.putExtra(AudioPlayer.Companion.TRACK_EXTRA, track)
        startActivity(audioPlayerIntent)
    }

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val TEXT_EDIT_VALUE = ""
        private const val IS_VISIBLE_BUTTON = "IS_VISIBLE_BUTTON"
    }
}
