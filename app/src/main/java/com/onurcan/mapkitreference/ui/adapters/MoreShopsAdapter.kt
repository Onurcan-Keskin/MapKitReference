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

class MoreShopsAdapter(
    context: Context, lat: Double,
    long: Double,
    hMap: HuaweiMap?,
    dialog: Dialog,
) : RecyclerView.Adapter<MoreShopsAdapter.ViewHolder>() {

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
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.department_store))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            1 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.shopping_center))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            2 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.coffee_shop))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            3 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.food_market))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            4 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.convenience_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            5 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.grocery))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            6 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.bookstore))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            7 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.cd_dvd_video_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            8 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.consumer_electronics_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            9 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.real_estate_agency_shop)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            10 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.mobile_phone_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            11 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.toys_and_games_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            12 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.gift_store))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            13 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.meat_store))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            14 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.fish_store))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            15 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.wine_spirits_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            16 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.jewelry_clock_and_watch_shop)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            17 -> {
                holder.bindMain(R.drawable.ic_market, mContext.getString(R.string.hobby_shop))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
            18 -> {
                holder.bindMain(
                    R.drawable.ic_market,
                    mContext.getString(R.string.musical_instrument_store)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    mDialog.dismiss()
                    holder.searchPOIWithItems(mContext, mLat, mLong, mHMap)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 19
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
                context.getString(R.string.department_store) -> {
                    request.hwPoiType = HwLocationType.DEPARTMENT_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.shopping_center) -> {
                    request.hwPoiType = HwLocationType.SHOPPING_CENTER
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.coffee_shop) -> {
                    request.hwPoiType = HwLocationType.COFFEE_SHOP
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.food_market) -> {
                    request.hwPoiType = HwLocationType.FOOD_MARKET
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.convenience_store) -> {
                    request.hwPoiType = HwLocationType.CONVENIENCE_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.grocery) -> {
                    request.hwPoiType = HwLocationType.GROCERY
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.bookstore) -> {
                    request.hwPoiType = HwLocationType.BOOKSTORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.cd_dvd_video_store) -> {
                    request.hwPoiType = HwLocationType.CD_DVD_VIDEO_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.consumer_electronics_store) -> {
                    request.hwPoiType = HwLocationType.CONSUMER_ELECTRONICS_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.real_estate_agency_shop) -> {
                    request.hwPoiType = HwLocationType.REAL_ESTATE_AGENCY_SHOP
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.mobile_phone_store) -> {
                    request.hwPoiType = HwLocationType.MOBILE_PHONE_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.toys_and_games_store) -> {
                    request.hwPoiType = HwLocationType.TOYS_AND_GAMES_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.gift_store) -> {
                    request.hwPoiType = HwLocationType.GIFT_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.meat_store) -> {
                    request.hwPoiType = HwLocationType.MEAT_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.fish_store) -> {
                    request.hwPoiType = HwLocationType.FISH_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.wine_spirits_store) -> {
                    request.hwPoiType = HwLocationType.WINE_SPIRITS_STORE
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.jewelry_clock_and_watch_shop) -> {
                    request.hwPoiType = HwLocationType.JEWELRY_CLOCK_AND_WATCH_SHOP
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.hobby_shop) -> {
                    request.hwPoiType = HwLocationType.HOBBY_SHOP
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.musical_instrument_store) -> {
                    request.hwPoiType = HwLocationType.MUSICAL_INSTRUMENT_STORE
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

