package com.devmanishpatole.imagecommenter.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

@ExperimentalCoroutinesApi
fun EditText.getTextChangeFlow(): StateFlow<String> {
    val query = MutableStateFlow("")

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            //No Implementation
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            //No Implementation
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            text?.let {
                query.value = it.toString()
            }
        }
    })

    return query
}




