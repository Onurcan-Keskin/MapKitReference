package com.onurcan.mapkitreference.model

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.Marker
import com.onurcan.mapkitreference.R


class CustomInfoWindowModel(context: Context) : HuaweiMap.InfoWindowAdapter {

    private val ctx = context
    override fun getInfoContents(marker: Marker?): View {
        val view: View = (ctx as Activity).layoutInflater
            .inflate(R.layout.single_info_vindow, null)
        val pName = view.findViewById<TextView>(R.id.iw_poi_name)
        pName.text = marker!!.title
        return view
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}