package com.didik.noteapps.extensions

import android.view.View

fun View.isShow(isShow: Boolean) {
    this.visibility = if (isShow) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}