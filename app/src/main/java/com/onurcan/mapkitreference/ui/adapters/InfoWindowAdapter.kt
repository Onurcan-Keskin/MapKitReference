package com.onurcan.mapkitreference.ui.adapters

import android.app.Activity
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.huawei.hms.maps.HuaweiMap.InfoWindowAdapter
import com.huawei.hms.maps.model.Marker
import com.onurcan.mapkitreference.R


internal class InfoWindowAdapter(context: Activity) : InfoWindowAdapter {
    private val mParis: Marker? = null
    private val mWindowView: View = context.layoutInflater.inflate(R.layout.info_window, null)
    private val mContentsView: View = context.layoutInflater.inflate(R.layout.info_contents, null)
    override fun getInfoWindow(marker: Marker): View {
        val view: View? = null
//        Log.d(TAG, "getInfoWindow")
//        if (mWindowType !== 3) {
//            return view!!
//        }
        render(marker, mWindowView)
        return mWindowView
    }

    override fun getInfoContents(marker: Marker): View {
        val view: View? = null
//        Log.d(TAG, "getInfoContents")
//        if (mWindowType !== 2) {
//            return view!!
//        }
        render(marker, mContentsView)
        return mContentsView
    }

    private fun render(marker: Marker, view: View) {
        setMarkerBadge(marker, view)
        setMarkerTextView(marker, view)
        setMarkerSnippet(marker, view)
    }

    private fun setMarkerBadge(marker: Marker, view: View) {
        val markerBadge: Int
        // Use the equals method to determine if the marker is the same ,do not use"=="
        when (marker) {
            mParis -> {
                markerBadge = R.drawable.badge_bj
            }
            else -> {
                markerBadge = 0
            }
        }
        (view.findViewById<View>(R.id.imgv_badge) as ImageView).setImageResource(markerBadge)
    }

    private fun setMarkerTextView(marker: Marker, view: View) {
        val markerTitle = marker.title
        var titleView: TextView? = null
        val `object`: Any = view.findViewById(R.id.txtv_titlee)
        if (`object` is TextView) {
            titleView = `object`
        }
        if (markerTitle == null) {
            titleView!!.text = ""
        } else {
            val titleText = SpannableString(markerTitle)
            titleText.setSpan(ForegroundColorSpan(Color.BLUE), 0, titleText.length, 0)
            titleView!!.text = titleText
        }
    }

    private fun setMarkerSnippet(marker: Marker, view: View) {
        var markerSnippet = marker.snippet
        if (marker.tag != null) {
            markerSnippet = marker.tag as String
        }
        val snippetView = view.findViewById<View>(R.id.txtv_snippett) as TextView
        if (markerSnippet != null && markerSnippet.isNotEmpty()) {
            val snippetText = SpannableString(markerSnippet)
            snippetText.setSpan(ForegroundColorSpan(Color.RED), 0, markerSnippet.length, 0)
            snippetView.text = snippetText
        } else {
            snippetView.text = ""
        }
    }

}