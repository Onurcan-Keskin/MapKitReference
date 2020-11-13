package com.onurcan.mapkitreference.ui.adapters

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.net.URLEncoder
import java.util.*

class AroundMeHorizontalPoiAdapter(
    context: Context,
    lat: Double,
    long: Double,
    poiItemRecyclerView: RecyclerView,
    lottieErrorHolder: ConstraintLayout,
    aroundMeSlideUpPanel: SlidingUpPanelLayout,
    hMap: HuaweiMap?
) :
    RecyclerView.Adapter<AroundMeHorizontalPoiAdapter.ViewHolder>() {

    private var mContext = context
    private var mLat = lat
    private var mLong = long
    private var mPoiItemRecyclerView = poiItemRecyclerView
    private var mLottieErrorHolder = lottieErrorHolder
    private var mAroundMeSlideUpPanel = aroundMeSlideUpPanel
    private var mHMap = hMap

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_poi_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.bindAMHorizontal(R.drawable.ic_bank, mContext.getString(R.string.poi_bank))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_bank)
                    )
                }
            }
            1 -> {
                holder.bindAMHorizontal(
                    R.drawable.ic_toilet,
                    mContext.getString(R.string.poi_restroom)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_restroom)
                    )
                }
            }
            2 -> {
                holder.bindAMHorizontal(R.drawable.ic_bus, mContext.getString(R.string.poi_busStop))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_busStop)
                    )
                }
            }
            3 -> {
                holder.bindAMHorizontal(R.drawable.ic_metro, mContext.getString(R.string.poi_metro))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_metro)
                    )
                }
            }
            4 -> {
                holder.bindAMHorizontal(R.drawable.ic_tram, mContext.getString(R.string.poi_tram))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_tram)
                    )
                }
            }
            5 -> {
                holder.bindAMHorizontal(R.drawable.ic_hotel, mContext.getString(R.string.poi_hotel))
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_hotel)
                    )
                }
            }
            6 -> {
                holder.bindAMHorizontal(
                    R.drawable.ic_p2s,
                    mContext.getString(R.string.poi_places_to_see)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_places_to_see)
                    )
                }
            }
            7 -> {
                holder.bindAMHorizontal(
                    R.drawable.ic_museum,
                    mContext.getString(R.string.poi_museum)
                )
                holder.parent.setOnClickListener {
                    mHMap!!.clear()
                    holder.aroundMe(
                        mContext,
                        mLat,
                        mLong,
                        mHMap,
                        mPoiItemRecyclerView,
                        mLottieErrorHolder,
                        mAroundMeSlideUpPanel,
                        mContext.getString(R.string.poi_museum)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 8
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView
        private var poiText: TextView

        init {
            poiText = parent.findViewById(R.id.single_poi_txt)
        }

        fun bindAMHorizontal(poiImage: Int, poiName: String) {
            poiText.text = poiName
            poiText.setCompoundDrawablesWithIntrinsicBounds(poiImage, 0, 0, 0)
        }

        fun aroundMe(
            context: Context,
            lat: Double,
            long: Double,
            hMap: HuaweiMap?,
            recyclerView: RecyclerView,
            lottieErrorHolder: ConstraintLayout,
            aroundMeSlideUpPanel: SlidingUpPanelLayout,
            text: String
        ) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val location = Coordinate(lat, long)
            val addressDetail = geocoder.getFromLocation(lat, long, 1)
            val request = NearbySearchRequest()
            request.location = location
            request.query = addressDetail[0].countryName
            request.query = "Turkey"
            request.radius = 1000

            request.language = "tr"
            request.pageIndex = 1
            request.pageSize = 5

            when (text) {
                context.getString(R.string.poi_bank) -> {
                    request.hwPoiType = HwLocationType.BANK
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_restroom) -> {
                    request.hwPoiType = HwLocationType.TOILET
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_busStop) -> {
                    request.hwPoiType = HwLocationType.BUS_STOP
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_metro) -> {
                    request.hwPoiType = HwLocationType.METRO
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_tram) -> {
                    request.hwPoiType = HwLocationType.TRAM_STOP
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_taxi) -> {
                    request.hwPoiType = HwLocationType.TAXI_STAND
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_hotel) -> {
                    request.hwPoiType = HwLocationType.HOTEL
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_places_to_see) -> {
                    request.hwPoiType = HwLocationType.IMPORTANT_TOURIST_ATTRACTION
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                context.getString(R.string.poi_museum) -> {
                    request.hwPoiType = HwLocationType.MUSEUM
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
                else -> {
                    request.hwPoiType = HwLocationType.ADDRESS
                    searchTypeNearby(
                        context,
                        request,
                        hMap,
                        recyclerView,
                        lottieErrorHolder,
                        aroundMeSlideUpPanel
                    )
                }
            }
        }

        private fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String) {
            if (null == hMap) return
            hMap.setMarkersClustering(true)
            hMap.addMarker(
                MarkerOptions().position(lng).title(pinName).clusterable(true).draggable(false)
            )
        }

        private fun searchTypeNearby(
            context: Context, request: NearbySearchRequest, hMap: HuaweiMap?,
            recyclerView: RecyclerView,
            lottieErrorHolder: ConstraintLayout,
            aroundMeSlideUpPanel: SlidingUpPanelLayout
        ) {
            val searchService = SearchServiceFactory.create(
                context,
                URLEncoder.encode(Constants.apiKey, "utf-8")
            )
            val resultListener = object : SearchResultListener<NearbySearchResponse> {
                override fun onSearchResult(p0: NearbySearchResponse?) {
                    if (p0 == null || p0.totalCount <= 0) {
                        recyclerView.visibility = View.GONE
                        lottieErrorHolder.visibility = View.VISIBLE
                        return
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        lottieErrorHolder.visibility = View.GONE
                    }

                    val sites = p0.sites

                    if (sites == null || sites.size == 0) {
                        recyclerView.visibility = View.GONE
                        lottieErrorHolder.visibility = View.VISIBLE
                        return
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        lottieErrorHolder.visibility = View.GONE
                    }

                    for (site in sites) {
                        showLogDebug(
                            Constants.mMapHelperPresenterTag,
                            String.format("siteId: '%s', name: %s\r\n", site.siteId, site.name)
                        )
                        addMarker(hMap, LatLng(site.location.lat, site.location.lng), site.name)
                        setAroundMeRecycler(
                            context,
                            recyclerView,
                            sites,
                            hMap,
                            aroundMeSlideUpPanel
                        )
                    }
                }

                override fun onSearchError(p0: SearchStatus?) {
                    showLogError(
                        Constants.mMapHelperPresenterTag,
                        "Error : " + p0!!.errorCode + " " + p0.errorMessage
                    )
                }
            }
            searchService.nearbySearch(request, resultListener)
        }

        private fun setAroundMeRecycler(
            context: Context,
            recyclerView: RecyclerView,
            sites: MutableList<Site>,
            hMap: HuaweiMap?,
            slideUpPanel: SlidingUpPanelLayout
        ) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = AroundMeAdapter(context, sites, hMap, slideUpPanel)
            recyclerView.itemAnimator = DefaultItemAnimator()
            setLayoutAnimation(recyclerView)
            recyclerView.adapter = adapter
        }

        private fun setLayoutAnimation(recyclerView: RecyclerView) {
            val context = recyclerView.context
            val layoutAnimController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
            recyclerView.layoutAnimation = layoutAnimController
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }
    }
}