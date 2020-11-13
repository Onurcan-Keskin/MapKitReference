package com.onurcan.mapkitreference.helper

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.onurcan.mapkitreference.R

fun setRecycleController(imageView: ImageView, recyclerView: RecyclerView, context: Context){
    if(recyclerView.visibility== View.GONE){
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down))
    } else{
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up))
    }
}