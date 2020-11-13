package com.onurcan.mapkitreference.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.util.*

class BookmarkRecentAdapter(
    val context: Context,
    recyclerView: RecyclerView,
    huaweiMap: HuaweiMap,
    slideUpPanel: SlidingUpPanelLayout
) : RecyclerView.Adapter<BookmarkRecentAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var poiItem = emptyList<PoiItemDataClass>()
    private val mContext = context
    private val mRecyclerView = recyclerView
    private val mHuaweiMap = huaweiMap
    private val mSlidingUpPanelLayout = slideUpPanel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.single_poi_card_roomdb, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = poiItem[position]
        if (currentItem.listName.decapitalize(Locale.ROOT).trim() == "favorites" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favorite" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favori" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favoriler"
        ) {
            holder.poiListImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_heart
                )
            )
        } else if (currentItem.listName.decapitalize(Locale.ROOT).trim() == "home" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "ev"
        ) {
            holder.poiListImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_home
                )
            )
        } else {
            holder.poiListImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_list
                )
            )
        }

        holder.poiName.text = currentItem.poiName
        holder.poiInfo.text = currentItem.poiAddress
        holder.poiListName.text = currentItem.listName
        val latLng = LatLng(currentItem.poiLat.toDouble(), currentItem.poiLong.toDouble())
        val pName = currentItem.poiAddress
        holder.poiLinLayout.setOnClickListener {
            mSlidingUpPanelLayout.visibility = View.GONE
            addMarker(mHuaweiMap, latLng, pName)
            moveCamera(mHuaweiMap, latLng)
        }

        val days5 = 1000 * 60 * 24 * 5
        if (System.currentTimeMillis() - currentItem.poiTimestamp.toLong() > days5) {
            // More than 5 days
            holder.parent.visibility = View.GONE
            holder.parent.layoutParams = RecyclerView.LayoutParams(0, 0)
            mRecyclerView.requestLayout()
        } else {
            holder.parent.visibility = View.VISIBLE
        }
    }

    private fun moveCamera(hMap: HuaweiMap?, latLng: LatLng) {
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(20F)
            //.bearing(31.5F)
            .tilt(2.2F)
            .build()
        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap!!.animateCamera(cu)
    }

    private fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String) {
        if (null == hMap) return
        hMap.setMarkersClustering(true)
        hMap.addMarker(
            MarkerOptions().position(lng).title(pinName).clusterable(true).draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_star_pin))
        )
    }

    override fun getItemCount(): Int {
        return poiItem.size
    }

    internal fun setItem(list: List<PoiItemDataClass>) {
        this.poiItem = list
        notifyDataSetChanged()
    }

    fun getItemPosition(position: Int): PoiItemDataClass {
        return poiItem[position]
    }

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView
        val poiName: TextView
        val poiInfo: TextView
        val poiListImg: ImageView
        val poiListName: TextView
        val poiLinLayout: LinearLayout

        init {
            poiName = parent.findViewById(R.id.single_poi_address)
            poiInfo = parent.findViewById(R.id.single_poi_info)
            poiListImg = parent.findViewById(R.id.single_poi_list_img)
            poiListName = parent.findViewById(R.id.single_poi_list_name)
            poiLinLayout = parent.findViewById(R.id.single_poi_ly)
        }
    }
}
