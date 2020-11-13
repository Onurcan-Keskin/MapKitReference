package com.onurcan.mapkitreference.ui.adapters

import android.app.Dialog
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.helper.Constants
import com.onurcan.mapkitreference.helper.showLogDebug
import com.onurcan.mapkitreference.helper.showLogError
import com.onurcan.mapkitreference.helper.showToast
import java.net.URLEncoder
import java.util.*

class MoreHolyAdapter(
    context: Context, lat: Double,
    long: Double,
    hMap: HuaweiMap?,
    dialog: Dialog
) : RecyclerView.Adapter<MoreHolyAdapter.ViewHolder>() {

    private val mContext = context
    private val mLat = lat
    private val mLong = long
    private val mHMap = hMap
    private val mDialog = dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_more_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.bindMain(
                    R.drawable.ic_holysite,
                    mContext.getString(R.string.place_of_worship)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            1 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.church))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            2 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.mosque))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            3 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.synagogue))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            4 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.temple))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            5 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.gurudwara))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            6 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.ashram))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            7 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.pagoda))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            8 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.shinto_shrine))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            9 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.hindu_temple))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            10 -> {
                holder.bindMain(
                    R.drawable.ic_holysite,
                    mContext.getString(R.string.buddhist_temple)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            11 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.jain_temple))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            12 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.mormon_church))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            13 -> {
                holder.bindMain(R.drawable.ic_holysite, mContext.getString(R.string.dharma_temple))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat, mLong, mHMap
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 14
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView

        private var poiText: TextView

        init {
            poiText = parent.findViewById(R.id.single_poi_txt)
        }

        fun bindMain(mPoiImage: Int, mPoiName: String) {
            poiText.text = mPoiName
            poiText.setCompoundDrawablesWithIntrinsicBounds(mPoiImage, 0, 0, 0)
        }

        fun searchPOIWithItems(
            context: Context,
            lat: Double,
            long: Double,
            hMap: HuaweiMap?
        ) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val location = Coordinate(lat, long)
            val request = NearbySearchRequest()
            request.location = location
            request.query = "Turkey"
            request.radius = 1000
            request.language = "tr"
            request.pageIndex = 1
            request.pageSize = 5
            when (poiText.text.toString()) {
                context.getString(R.string.place_of_worship) -> {
                    request.hwPoiType = HwLocationType.PLACE_OF_WORSHIP
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.church) -> {
                    request.hwPoiType = HwLocationType.CHURCH
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.mosque) -> {
                    request.hwPoiType = HwLocationType.MOSQUE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.synagogue) -> {
                    request.hwPoiType = HwLocationType.SYNAGOGUE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.temple) -> {
                    request.hwPoiType = HwLocationType.TEMPLE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.gurudwara) -> {
                    request.hwPoiType = HwLocationType.GURUDWARA
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.ashram) -> {
                    request.hwPoiType = HwLocationType.ASHRAM
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.pagoda) -> {
                    request.hwPoiType = HwLocationType.PAGODA
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.shinto_shrine) -> {
                    request.hwPoiType = HwLocationType.SHINTO_SHRINE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.hindu_temple) -> {
                    request.hwPoiType = HwLocationType.HINDU_TEMPLE
                    searchTypeText(context, request, hMap)
                }
                else -> {
                    showToast(context, "Text not matched.")
                }
            }
        }

        private fun searchTypeText(context: Context, request: NearbySearchRequest, hMap: HuaweiMap?) {
            val searchService = SearchServiceFactory.create(
                context,
                URLEncoder.encode(Constants.apiKey, "utf-8")
            )
            val resultListener = object : SearchResultListener<NearbySearchResponse> {
                override fun onSearchResult(response: NearbySearchResponse?) {
                    if (response == null || response.totalCount <= 0) {
                        showToast(context, context.getString(R.string.error_item))
                        return
                    }
                    val sites = response.sites
                    if (sites == null || sites.size == 0)
                        return
                    for (site in sites) {
                        showLogDebug(
                            "searchPOI",
                            String.format(
                                "siteId: '%s', name : %s, location : %s,%s\r\n",
                                site.siteId,
                                site.name,
                                site.location.lat.toString(),
                                site.location.lng.toString()
                            )
                        )
                        addMarker(hMap, LatLng(site.location.lat, site.location.lng), site.name)
                    }
                }

                override fun onSearchError(p0: SearchStatus?) {
                    showLogError(
                        "searchPOI",
                        "Error : " + p0!!.errorCode + " " + p0.errorMessage
                    )
                }
            }

            searchService.nearbySearch(request, resultListener)
        }

        fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String) {
            if (null == hMap) return
            hMap.setMarkersClustering(true)
            hMap.addMarker(
                MarkerOptions().position(lng).title(pinName).clusterable(true).draggable(false)
            )
        }
    }
}