package com.onurcan.mapkitreference.ui.contracts

import android.content.Context
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.roughike.bottombar.BottomBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MapKitMainContract {
    interface ViewMainMap {
        fun uiVisible(
            mEditText: MaterialCardView,
            mLinearPoi: RecyclerView,
            mGo: MaterialCardView,
            mDetails: MaterialCardView
        )
        fun uiGone(
            mEditText: MaterialCardView,
            mLinearPoi: LinearLayout,
            mGo: MaterialCardView,
            mDetails: MaterialCardView,
            mDistance: ConstraintLayout
        )
        fun setLightPopUp(context: Context, mDetails: MaterialCardView, hMap: HuaweiMap)
        fun setDarkPopUp(context: Context, mDetails: MaterialCardView, hMap: HuaweiMap)
        fun moveCamera(hMap: HuaweiMap?, latLng: LatLng)
        fun setPrefs(
            sharedPrefsManager: SharedPrefsManager,
            context: Context,
            slidePin: RelativeLayout,
            slideAround: LinearLayout,
            animatedBottomBar: BottomBar,
            hMap: HuaweiMap?
        )
    }

    interface PresenterMainMap {
        fun mapClicked(
            mEditText: MaterialCardView,
            mGo: MaterialCardView,
            mDetails: MaterialCardView,
            mAnim: BottomBar,
            mRecycler: RecyclerView,
            mDistance: ConstraintLayout
        )
        fun copyAddressToClipboard(context: Context, addressTextView: TextView)
        fun setPinTitle(lat: Double, long: Double, context: Context): String
        fun onGoClicked(
            fromTo: CoordinatorLayout,
            materialEditCard: MaterialCardView,
            horizontalPoi: RecyclerView,
            mapDetails: MaterialCardView,
            goBy: ConstraintLayout,
            mapGo: MaterialCardView,
        )
        fun setMainPoiRecycler(
            context: Context,
            recyclerView: RecyclerView,
            lat: Double,
            long: Double,
            hMap: HuaweiMap?,
            sharedPrefsManager: SharedPrefsManager
        )
        fun showSheetFromPlacedMarker(
            context: Context,
            slideUpPanel: SlidingUpPanelLayout,
            lat: Double,
            long: Double,
            poiName: TextView,
            poiAddress: TextView,
            poiAddressLin: LinearLayout,
            poiHours: TextView,
            marker: Marker
        )
        fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String)
    }
}