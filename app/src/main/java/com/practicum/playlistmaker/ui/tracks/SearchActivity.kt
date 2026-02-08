package com.practicum.playlistmaker.ui.tracks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.ui.audioplayer.AudioPlayer
//import com.practicum.playlistmaker.PM_PREFERENCES
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.domain.Creator
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track
const val PM_PREFERENCES = "playlistmaker"
const val SEARCH_TRACK_HISTORY_KEY = "searchTrackHistory"

class SearchActivity : AppCompatActivity() {

    private val tracks = mutableListOf<Track>()

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }
    private var constTextEdit: String = TEXT_EDIT_VALUE
    private lateinit var inputEditText: EditText
    private lateinit var trackList: RecyclerView
    private lateinit var trackListSearch: RecyclerView
    private lateinit var errorNoInternet: LinearLayout
    private lateinit var errorNoData: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var historyLayout: LinearLayout
    private lateinit var loadTracksInteractor: TracksInteractor
    private val adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private var constIsClearButtonVisible: Int = 8

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(PM_PREFERENCES, MODE_PRIVATE)//?
        val searchHistory = SearchHistory(sharedPreferences)//?

        loadTracksInteractor = Creator.provideTracksInteractor()
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            constTextEdit = savedInstanceState.getString(EDIT_TEXT, TEXT_EDIT_VALUE)
            constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON, 0)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContentView(R.layout.activity_search)

        val main = findViewById<Toolbar>(R.id.back)
        main.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        trackList = findViewById(R.id.recyclerView)
        trackListSearch = findViewById(R.id.recyclerHistoryView)
        errorNoInternet = findViewById(R.id.errorInternet)
        errorNoData = findViewById(R.id.errorNoData)
        updateButton = findViewById(R.id.updateButton)
        clearHistoryButton = findViewById(R.id.clearButton)
        progressBar = findViewById(R.id.progressBar)
        historyLayout = findViewById(R.id.historyLinearLayout)

        adapter.tracks = tracks

        adapter.onTrackClick = { track ->
            searchHistory.addTrack(track)
            val history = searchHistory.getHistory()
            historyAdapter.tracks = history
            historyAdapter.notifyDataSetChanged()
            if (clickDebounce()) {
                val audioPlayerIntent = Intent(this, AudioPlayer::class.java)
                audioPlayerIntent.putExtra(AudioPlayer.Companion.TRACK_EXTRA, track)
                startActivity(audioPlayerIntent)
            }
        }

        historyAdapter.onTrackClick = { track ->
            val audioPlayerIntent = Intent(this, AudioPlayer::class.java)
            audioPlayerIntent.putExtra(AudioPlayer.Companion.TRACK_EXTRA, track)
            startActivity(audioPlayerIntent)
        }

        hideData()

        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = adapter
        trackListSearch.adapter = historyAdapter

        inputEditText.setText(constTextEdit.toString())
        clearButton.visibility = constIsClearButtonVisible

        clearButton.setOnClickListener {
            inputEditText.setText("")
            closeKeyboard()
            errorNoInternet.visibility = View.GONE
            errorNoData.visibility = View.GONE
            trackList.visibility = View.GONE
            if (searchHistory.getHistory().isNotEmpty()) {
                historyLayout.visibility = View.VISIBLE
            } else
                historyLayout.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                constTextEdit = inputEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
                constIsClearButtonVisible = clearButton.visibility

                if (inputEditText.hasFocus() && s?.isEmpty() == true && searchHistory.getHistory()
                        .isNotEmpty()
                )
                    showHistory()
                else
                    historyLayout.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        updateButton.setOnClickListener {
            search()
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clear()//??
            val history = searchHistory.getHistory()//??
            historyAdapter.tracks = history
            historyAdapter.notifyDataSetChanged()
            historyLayout.visibility = View.GONE
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && searchHistory.getHistory()//??
                    .isNotEmpty()
            ) {
                showHistory()
                val history = searchHistory.getHistory()///??
                historyAdapter.tracks = history //??
                historyAdapter.notifyDataSetChanged()
            } else historyLayout.visibility = View.GONE
        }

        //реакция на нажатие DONE
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            true
        }
        false
    }

    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            hideData()

            loadTracksInteractor.searchTracks(
                inputEditText.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(result: Result<List<Track>>) {
                        runOnUiThread {
                            result.onSuccess { foundTracks ->
                                if (foundTracks.isEmpty()) {
                                    tracks.clear()
                                    adapter.notifyDataSetChanged()
                                    showErrorNoData()
                                } else {
                                    tracks.clear()
                                    tracks.addAll(foundTracks)
                                    adapter.notifyDataSetChanged()
                                    showSearchResults()
                                }
                            }
                                .onFailure {
                                    tracks.clear()
                                    showErrorNoInternet()
                                    adapter.notifyDataSetChanged()

                                }
                        }
                    }
                })
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, constTextEdit)
        outState.putInt(IS_VISIBLE_BUTTON, constIsClearButtonVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        constTextEdit = savedInstanceState.getString(EDIT_TEXT, TEXT_EDIT_VALUE)
        constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON, 0)
    }

    private fun showSearchResults() {
        // Скрываем placeholder и показываем результаты
        errorNoInternet.visibility = View.GONE
        errorNoData.visibility = View.GONE
        progressBar.visibility = View.GONE
        trackList.visibility = View.VISIBLE
        historyLayout.visibility = View.GONE
        closeKeyboard()
    }

    private fun showHistory() {
        errorNoInternet.visibility = View.GONE
        errorNoData.visibility = View.GONE
        progressBar.visibility = View.GONE
        trackList.visibility = View.GONE
        historyLayout.visibility = View.VISIBLE
    }

    private fun showErrorNoInternet() {
        errorNoInternet.visibility = View.VISIBLE
        errorNoData.visibility = View.GONE
        trackList.visibility = View.GONE
        progressBar.visibility = View.GONE
        closeKeyboard()
    }

    private fun showErrorNoData() {
        errorNoInternet.visibility = View.GONE
        errorNoData.visibility = View.VISIBLE
        trackList.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun hideData() {
        errorNoInternet.visibility = View.GONE
        errorNoData.visibility = View.GONE
        trackList.visibility = View.GONE
        historyLayout.visibility = View.GONE
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
        //отменяем активные запросы при выходе из экрана
    }

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val TEXT_EDIT_VALUE = ""
        private const val IS_VISIBLE_BUTTON = "IS_VISIBLE_BUTTON"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}