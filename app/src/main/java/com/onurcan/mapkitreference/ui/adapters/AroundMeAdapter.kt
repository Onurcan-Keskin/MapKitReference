package com.onurcan.mapkitreference.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.site.api.model.Site
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.ui.activities.WebViewActivity
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.squareup.picasso.Picasso

class AroundMeAdapter(
    context: Context,
    sites: MutableList<Site>,
    hMap: HuaweiMap?,
    slidingUpPanelLayout: SlidingUpPanelLayout
) :
    RecyclerView.Adapter<AroundMeAdapter.ViewHolder>() {

    var mContext = context
    private var mSites = sites
    private var mMap = hMap
    private var upPanelLayout = slidingUpPanelLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_around_me, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mSites[position].poi.websiteUrl != null &&
            mSites[position].poi.websiteUrl.isNotEmpty()
        ) {
            holder.bindAroundMe(
                "https://trendland.com/wp-content/uploads/2019/03/editorial-illustration-by-spiros-halaris-3.jpg",
                mSites[position].name,
                mSites[position].poi.websiteUrl,
                mSites[position].formatAddress
            )
            holder.poiWebsite.setOnClickListener {

                mContext.startActivity(Intent(it.context, WebViewActivity::class.java)
                    .putExtra("url",holder.poiWebsite.text.toString())
                    .putExtra("place",holder.poiName.text.toString()))
            }
        } else {
            holder.bindAroundMe(
                "https://trendland.com/wp-content/uploads/2019/03/editorial-illustration-by-spiros-halaris-3.jpg",
                mSites[position].name,
                "",
                mSites[position].formatAddress
            )
            holder.poiWebsite.visibility = View.GONE
        }
        holder.parent.setOnClickListener {
            upPanelLayout.visibility = View.GONE
            val cameraPosition = CameraPosition.builder()
                .target(LatLng(mSites[position].location.lat, mSites[position].location.lng))
                .zoom(20F)
                //.bearing(31.5F)
                .tilt(2.2F)
                .build()
            val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mMap!!.animateCamera(cu)
        }
    }

    override fun getItemCount(): Int {
        return mSites.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView

        private var poiImage: ImageView
        var poiName: TextView
        var poiWebsite: TextView
        private var poiAddress: TextView
        var poiFrame: FrameLayout
        private var poiConstraint: ConstraintLayout


        init {
            poiImage = parent.findViewById(R.id.am_pImage)
            poiName = parent.findViewById(R.id.am_pName)
            poiWebsite = parent.findViewById(R.id.am_pWebsite)
            poiAddress = parent.findViewById(R.id.am_pAddress)
            poiFrame = parent.findViewById(R.id.am_frame)
            poiConstraint = parent.findViewById(R.id.am_constraint)
        }

        fun bindAroundMe(
            imageView: String,
            name: String,
            website: String,
            address: String
        ) {
            Picasso.get().load(imageView).placeholder(R.drawable.default_poi_image).centerCrop()
                .fit().into(poiImage)
            poiName.text = name
            poiWebsite.text = website
            poiAddress.text = address
        }
    }
}