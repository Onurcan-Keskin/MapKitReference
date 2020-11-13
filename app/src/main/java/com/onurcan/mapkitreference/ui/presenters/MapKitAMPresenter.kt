package com.onurcan.mapkitreference.ui.presenters

import android.content.Context
import android.location.Geocoder
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.onurcan.mapkitreference.ui.adapters.AroundMeAdapter
import com.onurcan.mapkitreference.ui.adapters.AroundMeHorizontalPoiAdapter
import com.onurcan.mapkitreference.ui.contracts.MapKitAMContract
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.net.URLEncoder
import java.util.*

class MapKitAMPresenter : MapKitAMContract.PresenterAM, MapKitAMContract.PresenterAMEX {

    override fun fillSheet(
        poiName: TextView,
        poiType: TextView,
        poiEta: TextView,
        poiAddress: TextView,
        poiHours: TextView,
        poiWebsite: TextView,
        site: Site,
        context: Context
    ) {
        poiName.text = site.name

        if (site.poi.hwPoiTypes[0].toString().isNotEmpty())
            poiType.text = site.poi.hwPoiTypes[0].toLowerCase(Locale.ROOT)
        else
            poiType.text = context.getText(R.string.emptyHolder)

        if (site.distance.toString().isNotEmpty() && site.distance.toString() != "NaN")
            poiEta.text = site.distance.toString()
        else
            poiEta.text = context.getText(R.string.emptyHolder)

        if (site.address.toString().isNotEmpty()
            && site.address != null
        )
            poiAddress.text = site.formatAddress
        else
            poiAddress.text = context.getText(R.string.emptyHolder)

//        if (site.poi.openingHours.toString().isNotEmpty()
//            && site.poi.openingHours != null
//        )
//            poiHours.text = site.poi.openingHours.texts.toString()
//        else
//            poiHours.text = context.getText(R.string.emptyHolder)

//        if (site.poi.websiteUrl.toString().isNotEmpty()
//            && site.poi.websiteUrl != null
//        )
//            poiWebsite.text = site.poi.websiteUrl
//        else
//            poiWebsite.text = context.getText(R.string.emptyHolder)
    }

    /* Global Sheet Listener */
    /**
     * @param slideUpPanel
     * */
    override fun listenSliderState(slideUpPanel: SlidingUpPanelLayout) {
        slideUpPanel.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                showLogDebug(Constants.mMapHelperPresenterTag, "Slide Offset $slideOffset")
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                when (newState) {
                    SlidingUpPanelLayout.PanelState.ANCHORED -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: ANCHORED")
                    }
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: COLLAPSED")
                        slideUpPanel.visibility = View.GONE
                    }
                    SlidingUpPanelLayout.PanelState.DRAGGING -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: DRAGGING")
                    }
                    SlidingUpPanelLayout.PanelState.EXPANDED -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: EXPANDED")
                    }
                    SlidingUpPanelLayout.PanelState.HIDDEN -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: HIDDEN")
                    }
                }
            }
        })
    }

    override fun onExplore(
        aroundMeSlideUpPanel: SlidingUpPanelLayout,
        poiEditText: EditText,
        hMap: HuaweiMap?
    ) {
        aroundMeSlideUpPanel.visibility = View.GONE
        poiEditText.setText("")
        hMap!!.clear()
    }

    override fun getAroundMeTextUItem(textView: TextView): String {
        return textView.text.toString()
    }

    override fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String) {
        if (null == hMap) return
        hMap.setMarkersClustering(true)
        hMap.addMarker(
            MarkerOptions().position(lng).title(pinName).clusterable(true).draggable(false)
        )
    }

    override fun onAroundMe(
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
       // val addressDetail = geocoder.getFromLocation(lat, long, 1)
        val request = NearbySearchRequest()
        request.location = location
        //request.query = addressDetail[0].countryName
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

    override fun setAMRecycler(
        context: Context,
        recyclerViewPoiSelector: RecyclerView,
        lat: Double,
        long: Double,
        recyclerViewPoiItem: RecyclerView,
        lottieErrorHolder: ConstraintLayout,
        aroundMeSlideUpPanel: SlidingUpPanelLayout,
        hMap: HuaweiMap?
    ) {
        recyclerViewPoiSelector.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = AroundMeHorizontalPoiAdapter(
            context,
            lat,
            long,
            recyclerViewPoiItem,
            lottieErrorHolder,
            aroundMeSlideUpPanel,
            hMap
        )
        recyclerViewPoiSelector.adapter = adapter
    }

    override fun searchTypeNearby(
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
                    setAroundMeRecycler(context, recyclerView, sites, hMap, aroundMeSlideUpPanel)
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

    override fun setAroundMeRecycler(
        context: Context,
        recyclerView: RecyclerView,
        sites: MutableList<Site>,
        hMap: HuaweiMap?,
        slideUpPanel: SlidingUpPanelLayout
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AroundMeAdapter(context, sites, hMap, slideUpPanel)
        recyclerView.adapter = adapter
    }
}