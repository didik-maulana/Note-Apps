package com.didik.noteapps.extensions

import android.widget.EditText

fun EditText.getValue(): String {
    return text.toString()
}

fun EditText.isBlank(): Boolean {
    return getValue().isBlank()
}