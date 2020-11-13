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

class MoreRestaurantsAdapter(
    context:Context, lat: Double,
    long: Double,
    hMap: HuaweiMap?,
    dialog: Dialog
) : RecyclerView.Adapter<MoreRestaurantsAdapter.ViewHolder>(){

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
        when(position){
            0 ->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                mContext.getString(R.string.food_drink))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            1->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.take_away_restaurant))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            2->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.sushi_restaurant))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            3->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.yogurt_juice_bar))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            4->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.bistro))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            5->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.bar))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            6->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.cafeteria))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            7->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.cafe_pub))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            8->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.creperie))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            9->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.bakery))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
            10->{
                holder.bindMainHorizontal(R.drawable.ic_restaurant,
                    mContext.getString(R.string.macrobiotic_restaurant))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext,mLat,mLong,mHMap)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 11
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val parent = itemView

        var poiText:TextView

        init {
            poiText = parent.findViewById(R.id.single_poi_txt)
        }

        fun bindMainHorizontal(mPoiImage: Int, mPoiName: String) {
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
                context.getString(R.string.food_drink) -> {
                    request.hwPoiType = HwLocationType.FOOD_DRINK_SHOP
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.take_away_restaurant) -> {
                    request.hwPoiType = HwLocationType.TAKE_AWAY_RESTAURANT
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.sushi_restaurant) -> {
                    request.hwPoiType = HwLocationType.SUSHI_RESTAURANT
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.yogurt_juice_bar) -> {
                    request.hwPoiType = HwLocationType.YOGURT_JUICE_BAR
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.bistro) -> {
                    request.hwPoiType = HwLocationType.BISTRO
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.bar) -> {
                    request.hwPoiType = HwLocationType.BAR
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.cafeteria) -> {
                    request.hwPoiType = HwLocationType.CAFETERIA
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.cafe_pub) -> {
                    request.hwPoiType = HwLocationType.CAFE_PUB
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.creperie) -> {
                    request.hwPoiType = HwLocationType.CREPERIE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.bakery) -> {
                    request.hwPoiType = HwLocationType.BAKERY
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.macrobiotic_restaurant) -> {
                    request.hwPoiType = HwLocationType.MACROBIOTIC_RESTAURANT
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