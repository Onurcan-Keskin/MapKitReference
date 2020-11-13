package com.onurcan.mapkitreference.helper

import android.view.ViewGroup
import android.widget.TextView

private fun TextView.equalize(b: Boolean) {
    val params = this.layoutParams
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
    this.layoutParams = params
}

private fun TextView.increase() {
    val params = this.layoutParams
    params.height = 120
    this.layoutParams = params
}

fun TextView.indicateClicked() {
    val isClicked = when (this.maxHeight) {
        ViewGroup.LayoutParams.WRAP_CONTENT -> false
        else -> true
    }
    if (isClicked) increase()
    else equalize(true)
}

