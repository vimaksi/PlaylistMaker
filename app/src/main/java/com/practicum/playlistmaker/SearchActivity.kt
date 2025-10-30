package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible


class SearchActivity : AppCompatActivity() {
    private var constTextEdit: String = TEXT_EDIT_VALUE
    private var constIsClearButtonVisible : Int = 8
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
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

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

    companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val TEXT_EDIT_VALUE = ""
        private const val IS_VISIBLE_BUTTON = "IS_VISIBLE_BUTTON"
    }
}