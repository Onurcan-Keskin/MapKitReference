package com.onurcan.mapkitreference.ui.contracts

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.site.api.model.NearbySearchRequest
import com.huawei.hms.site.api.model.Site
import com.sothree.slidinguppanel.SlidingUpPanelLayout

/**
 *  @constructor Map Kit Around Me Contract
 */
class MapKitAMContract {

    interface ViewAM{}

    interface PresenterAM{
        fun fillSheet(
            poiName: TextView,
            poiType: TextView,
            poiEta: TextView,
            poiAddress: TextView,
            poiHours: TextView,
            poiWebsite: TextView,
            site: Site,
            context: Context
        )
        fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String)
        fun listenSliderState(slideUpPanel: SlidingUpPanelLayout)
        fun onExplore(
            aroundMeSlideUpPanel: SlidingUpPanelLayout,
            poiEditText: EditText,
            hMap: HuaweiMap?
        )
    }

    interface PresenterAMEX{
        fun getAroundMeTextUItem(textView: TextView): String
        fun onAroundMe(
            context: Context,
            lat: Double,
            long: Double,
            hMap: HuaweiMap?,
            recyclerView: RecyclerView,
            lottieErrorHolder: ConstraintLayout,
            aroundMeSlideUpPanel: SlidingUpPanelLayout,
            text: String
        )
        fun setAMRecycler(
            context: Context,
            recyclerViewPoiSelector: RecyclerView,
            lat: Double,
            long: Double,
            recyclerViewPoiItem: RecyclerView,
            lottieErrorHolder: ConstraintLayout,
            aroundMeSlideUpPanel: SlidingUpPanelLayout,
            hMap: HuaweiMap?
        )
        fun searchTypeNearby(
            context: Context, request: NearbySearchRequest, hMap: HuaweiMap?,
            recyclerView: RecyclerView,
            lottieErrorHolder: ConstraintLayout,
            aroundMeSlideUpPanel: SlidingUpPanelLayout
        )
        fun setAroundMeRecycler(
            context: Context,
            recyclerView: RecyclerView,
            sites: MutableList<Site>,
            hMap: HuaweiMap?,
            slideUpPanel: SlidingUpPanelLayout
        )
    }
}