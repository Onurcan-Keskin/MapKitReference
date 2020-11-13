package com.onurcan.mapkitreference.helper

import android.widget.TextView

fun TextView.minLine(b: Boolean){
    this.maxLines = 1
}

fun TextView.maxLine(){
    this.maxLines = 5
}

fun TextView.expandExplanation(){
    val isMaxThree = when(this.maxLines){
        1 -> true
        else -> false
    }
    if (isMaxThree){
        maxLine()
    } else {
        minLine(false)
    }
}