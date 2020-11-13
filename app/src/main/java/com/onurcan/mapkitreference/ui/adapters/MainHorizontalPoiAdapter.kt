package com.onurcan.mapkitreference.ui.adapters

import android.app.Dialog
import android.content.Context
import android.location.Geocoder
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.helper.*
import java.net.URLEncoder
import java.util.*

class MainHorizontalPoiAdapter(
    context: Context, lat: Double,
    long: Double,
    hMap: HuaweiMap?,
    sharedPrefsManager: SharedPrefsManager
) :
    RecyclerView.Adapter<MainHorizontalPoiAdapter.ViewHolder>() {

    private var mContext = context
    private var mLat = lat
    private var mLong = long
    private var mHMap = hMap
    private var mSharedPrefsManager = sharedPrefsManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_poi_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                /* Airport */
                holder.bindMainHorizontal(
                    R.drawable.ic_airport,
                    mContext.getString(R.string.poi_airport)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            1 -> {
                /* Cinema */
                holder.bindMainHorizontal(
                    R.drawable.ic_cinema,
                    mContext.getString(R.string.poi_cinema)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            2 -> {
                /* Gas */
                holder.bindMainHorizontal(
                    R.drawable.ic_gas_station,
                    mContext.getString(R.string.poi_gas)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            3 -> {
                /* Pharmacy */
                holder.bindMainHorizontal(
                    R.drawable.ic_pharmacy,
                    mContext.getString(R.string.poi_pharmacy)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            4 -> {
                /* Restaurant */
                holder.bindMainHorizontal(
                    R.drawable.ic_restaurant,
                    mContext.getString(R.string.poi_restaurant)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            5 -> {
                /* Police */
                holder.bindMainHorizontal(
                    R.drawable.ic_police,
                    mContext.getString(R.string.poi_police)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            6 -> {
                /* School */
                holder.bindMainHorizontal(
                    R.drawable.ic_school,
                    mContext.getString(R.string.poi_school)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            7 -> {
                /* Market */
                holder.bindMainHorizontal(
                    R.drawable.ic_market,
                    mContext.getString(R.string.poi_market)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.searchPOIWithItems(
                        mContext,
                        mLat,
                        mLong,
                        mHMap
                    )
                }
            }
            8 -> {
                /* More */
                holder.bindMainHorizontal(
                    R.drawable.ic_more_horiz,
                    mContext.getString(R.string.action_more)
                )
                holder.parent.setOnClickListener {
                    setMoreDialog(mContext, R.style.DialogSlide, mSharedPrefsManager)
                }
            }
            else -> {
                showToast(mContext, "Search POI has encountered error or POI is empty.")
            }
        }
    }

    override fun getItemCount(): Int {
        return 9
    }

    private fun setMoreDialog(context: Context, type: Int, sharedPrefsManager: SharedPrefsManager) {
        val dialog = Dialog(context, R.style.BlurTheme)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setElevation(3F)
        dialog.window!!.setLayout(width, height)
        dialog.window!!.attributes.windowAnimations = type
        dialog.setContentView(R.layout.dialog_more)
        dialog.setCanceledOnTouchOutside(false)

        val dConstraint = dialog.findViewById<ConstraintLayout>(R.id.more_constraint)
        val dAppBarLayout = dialog.findViewById<AppBarLayout>(R.id.d_appBarLayout)
        val dBack = dialog.findViewById<ImageView>(R.id.d_back)

        val dRestRecyclerView = dialog.findViewById<RecyclerView>(R.id.more_restaurant_recycler)
        val dRestImg = dialog.findViewById<ImageView>(R.id.more_restaurant_img)

        val dShopRecyclerView = dialog.findViewById<RecyclerView>(R.id.more_shops_recycler)
        val dShopImg = dialog.findViewById<ImageView>(R.id.more_shop_img)

        val dLeisureRecyclerView = dialog.findViewById<RecyclerView>(R.id.more_leisure_recycler)
        val dLeisureImg = dialog.findViewById<ImageView>(R.id.more_leisure_img)

        val dHolyRecyclerView = dialog.findViewById<RecyclerView>(R.id.more_holy_recycler)
        val dHolyImg = dialog.findViewById<ImageView>(R.id.more_holy_img)

        val dHealthRecyclerView = dialog.findViewById<RecyclerView>(R.id.more_health_recycler)
        val dHealthImg = dialog.findViewById<ImageView>(R.id.more_health_img)

        val dServiceRecyclerView = dialog.findViewById<RecyclerView>(R.id.more_service_recycler)
        val dServiceImg = dialog.findViewById<ImageView>(R.id.more_service_img)

        if (sharedPrefsManager.loadNightModeState()) {
            dConstraint.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_dark)
            dAppBarLayout.background =
                ContextCompat.getDrawable(context, R.drawable.action_bar_bg_dark)
        } else {
            dConstraint.background =
                ContextCompat.getDrawable(context, R.drawable.colour_bg_light_1)
            dAppBarLayout.background =
                ContextCompat.getDrawable(context, R.drawable.action_bar_bg_light)
        }

        /* ---------- Visibility ---------- */

        dRestImg.setOnClickListener {
            dRestRecyclerView.expandView()
            setRecycleController(dRestImg, dRestRecyclerView, context)
        }

        dShopImg.setOnClickListener {
            dShopRecyclerView.expandView()
            setRecycleController(dShopImg, dShopRecyclerView, context)
        }

        dLeisureImg.setOnClickListener {
            dLeisureRecyclerView.expandView()
            setRecycleController(dLeisureImg, dLeisureRecyclerView, context)
        }

        dHolyImg.setOnClickListener {
            dHolyRecyclerView.expandView()
            setRecycleController(dHolyImg, dHolyRecyclerView, context)
        }

        dHealthImg.setOnClickListener {
            dHealthRecyclerView.expandView()
            setRecycleController(dHealthImg, dHealthRecyclerView, context)
        }

        dServiceImg.setOnClickListener {
            dServiceRecyclerView.expandView()
            setRecycleController(dServiceImg, dServiceRecyclerView, context)
        }

        /* ---------- Recycler ---------- */

        dRestRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapterRest = MoreRestaurantsAdapter(context, mLat, mLong, mHMap, dialog)
        dRestRecyclerView.adapter = adapterRest

        dShopRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapterShop = MoreShopsAdapter(context, mLat, mLong, mHMap, dialog)
        dShopRecyclerView.adapter = adapterShop

        dLeisureRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapterLeisure = MoreLeisureAdapter(context, mLat, mLong, mHMap, dialog)
        dLeisureRecyclerView.adapter = adapterLeisure

        dHolyRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapterHoly = MoreHolyAdapter(context, mLat, mLong, mHMap, dialog)
        dHolyRecyclerView.adapter = adapterHoly

        dHealthRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapterHealth = MoreHealthAdapter(context, mLat, mLong, mHMap, dialog)
        dHealthRecyclerView.adapter = adapterHealth

        dServiceRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapterService = MoreServicesAdapter(context, mLat, mLong, mHMap, dialog)
        dServiceRecyclerView.adapter = adapterService

        dBack.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView

        private var poiText: TextView

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
            request.pageSize = 10
            when (poiText.text.toString()) {
                context.getString(R.string.poi_airport) -> {
                    request.hwPoiType = HwLocationType.AIRPORT
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_cinema) -> {
                    request.hwPoiType = HwLocationType.CINEMA
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_gas) -> {
                    request.hwPoiType = HwLocationType.PETROL_STATION
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_pharmacy) -> {
                    request.hwPoiType = HwLocationType.PHARMACY
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_restaurant) -> {
                    request.hwPoiType = HwLocationType.RESTAURANT
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_police) -> {
                    request.hwPoiType = HwLocationType.POLICE_STATION
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_school) -> {
                    request.hwPoiType = HwLocationType.SCHOOL
                    searchTypeText(context, request, hMap)
                }
                context.getString(R.string.poi_market) -> {
                    request.hwPoiType = HwLocationType.GROCERY
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