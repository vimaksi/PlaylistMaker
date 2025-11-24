package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private val trackBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(TrackApi::class.java)
    private val tracks = ArrayList<Track>()
    private var constTextEdit: String = TEXT_EDIT_VALUE
    private lateinit var inputEditText: EditText
    private lateinit var placeholderTextView: TextView
    private lateinit var placeholderButton: TextView
    private lateinit var trackList: RecyclerView
    //button
    val adapter = TrackAdapter()

    private var constIsClearButtonVisible : Int = 8
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            constTextEdit = savedInstanceState.getString(EDIT_TEXT, TEXT_EDIT_VALUE)
            constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON, 0 )
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContentView(R.layout.activity_search)

        val main = findViewById<Toolbar>(R.id.back)
        main.setOnClickListener{
            finish()
        }

        val frameLayout = findViewById<FrameLayout>(R.id.container)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        trackList = findViewById(R.id.recyclerView)
        placeholderTextView = findViewById(R.id.placeHolderTextView)
        placeholderButton = findViewById(R.id.placeHolderButton)

        adapter.tracks = tracks

        trackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        trackList.adapter = adapter

        inputEditText.setText(constTextEdit.toString())
        clearButton.visibility = constIsClearButtonVisible

        clearButton.setOnClickListener {
            inputEditText.setText("")
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                constTextEdit = inputEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
                constIsClearButtonVisible = clearButton.visibility
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)


        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (inputEditText.text.isNotEmpty()){
                    trackService.search(inputEditText.text.toString())
                        .enqueue(object: Callback<TrackResponse>{
                            override fun onResponse(
                                call: Call<TrackResponse?>,
                                response: Response<TrackResponse?>
                            ) {
                                when (response.code()){
                                    200 -> {
                                        if (response.body()?.results?.isNotEmpty() == true) {
                                            tracks.clear()
                                            tracks.addAll(response.body()?.results!!)
                                            adapter.notifyDataSetChanged()
                                            showSearchResults()
                                        }
                                        if (tracks.isEmpty()) {
                                            showMessage(getString(R.string.not_found), "")
                                        } else {
                                            showMessage("сбщ 2 чпето", "")
                                        }
                                    }
                                    else -> {
                                        showMessage(
                                            getString(R.string.problem_with_internet),
                                            response.code().toString()
                                        )
                                    }
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse?>, t: Throwable) {
                                showMessage(getString(R.string.problem_with_internet), t.message.toString())
                            }
                        })
                }
                true
            }
            false
        }
    }

    private fun showSearchResults() {
        // Скрываем placeholder и показываем результаты
        placeholderTextView.visibility = View.GONE
        placeholderButton.visibility = View.GONE
        trackList.visibility = View.VISIBLE
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
        outState.putInt(IS_VISIBLE_BUTTON,constIsClearButtonVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        constTextEdit = savedInstanceState.getString(EDIT_TEXT, TEXT_EDIT_VALUE)
        constIsClearButtonVisible = savedInstanceState.getInt(IS_VISIBLE_BUTTON,0)
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()){
            placeholderTextView.visibility = View.VISIBLE
            placeholderButton.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderTextView.text = text
            if (additionalMessage.isNotEmpty()){
                Toast.makeText(applicationContext,additionalMessage, Toast.LENGTH_LONG)
                    .show()
            } else {
                placeholderTextView.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val TEXT_EDIT_VALUE = ""
        private const val IS_VISIBLE_BUTTON = "IS_VISIBLE_BUTTON"
    }

//    val trackList = mutableListOf(
//        Track("Smells Like Teen Spirit","Nirvana","5:01","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
//        Track("Billie Jean","Michael Jackson","4:35","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
//        Track("Stayin' Alive","Bee Gees","4:10","https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
//        Track("Whole Lotta Love","Led Zeppelin","5:33","https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
//        Track("Sweet Child O'Mine","Guns N' Roses","5:03","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
//
//    )
}