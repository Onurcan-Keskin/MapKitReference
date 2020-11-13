package com.onurcan.mapkitreference.ui.contracts

import android.app.Activity
import android.content.Context
import android.location.Location
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.LatLngBounds
import com.huawei.hms.maps.model.Marker
import com.onurcan.mapkitreference.helper.SharedPrefsManager

/**
 *  @constructor Map Kit Bottom sheet Includer for poi Contract
 */
class MapKitBIContract {

    /* BI for Bottom Sheet Includer */
    interface ViewBI {
        fun getDirectionSuccess(pathList: ArrayList<LatLng>)
        fun getDirectionFailure(message: String)
    }

    interface PresenterBI {
        fun initLocation(context: Context)
        fun requestLocationUpdate()
        fun requestRemoveLocationUpdate()
        fun getLastKnownLocation(): Location?
    }

    /* BI Exclusive Interface */
    interface PresenterBIEX {
        fun generateRoute(
            json: String,
            context: Context,
            activity: Activity,
            poiEta: TextView,
            tvDistance: TextView,
            tvHolder: ConstraintLayout,
            hMap: HuaweiMap?
        )

        fun renderRoute(
            context: Context,
            paths: List<List<LatLng>>?,
            latLngBounds: LatLngBounds?,
            hMap: HuaweiMap?
        )

        fun requestGetDirectionWalking(
            context: Context,
            activity: Activity,
            destination: LatLng,
            poiEta: TextView,
            tvDistance: TextView,
            tvHolder: ConstraintLayout,
            hMap: HuaweiMap?
        )

        fun requestGetDirectionDriving(
            context: Context,
            activity: Activity,
            destination: LatLng,
            poiEta: TextView,
            tvDistance: TextView,
            tvHolder: ConstraintLayout,
            hMap: HuaweiMap?
        )

        fun requestGetDirectionBicycling(
            context: Context,
            activity: Activity,
            destination: LatLng,
            poiEta: TextView,
            tvDistance: TextView,
            tvHolder: ConstraintLayout,
            hMap: HuaweiMap?
        )

        fun addOriginMarker(context: Context, latLng: LatLng, hMap: HuaweiMap?)
        fun addDestinationMarker(context: Context, latLng: LatLng, hMap: HuaweiMap?)
        fun setPinTitle(lat: Double, long: Double, context: Context): String
        fun removePolylines()
        fun openWithHere(context: Context, marker: Marker)
        fun openWithGoogle(context: Context, marker: Marker)
        fun openWithOpen(context: Context, marker: Marker)
        fun shareFunHere(context: Context, marker: Marker)
        fun shareFunGoogle(context: Context, marker: Marker)
        fun shareFunOpen(context: Context, marker: Marker)
        fun startShareDialog(
            context: Context,
            marker: Marker,
            sharedPrefsManager: SharedPrefsManager,
            type: Int
        )
    }
}