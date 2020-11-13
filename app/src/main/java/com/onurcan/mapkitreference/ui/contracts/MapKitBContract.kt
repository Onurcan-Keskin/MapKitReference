package com.onurcan.mapkitreference.ui.contracts

import android.content.Context
import android.graphics.Bitmap
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.huawei.hms.maps.HuaweiMap
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.onurcan.mapkitreference.model.PoiItemModel
import com.onurcan.mapkitreference.ui.activities.MainActivity
import com.onurcan.mapkitreference.ui.adapters.PoiItemAdapter
import com.roughike.bottombar.BottomBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout

/**
 *  @constructor Map Kit Bookmark Contract
 */
class MapKitBContract {

    interface ViewB{
        fun listenSliderState(slideUpPanel: SlidingUpPanelLayout)
    }

    interface PresenterB{
        fun initLocation(context: Context)
        fun requestLocationUpdate()
        fun setBookmarkListAll(
            context: Context,
            activity: MainActivity,
            textView: TextView,
            slideUpPanel: SlidingUpPanelLayout,
            recyclerView: RecyclerView,
            lottieAnimationView: LottieAnimationView,
            notFoundText: TextView,
            linearLayout: LinearLayout,
            sharedPrefsManager: SharedPrefsManager,
            huaweiMap: HuaweiMap
        )
        fun setBookmarkListNearby(
            context: Context,
            activity: MainActivity,
            textView: TextView,
            slideUpPanel: SlidingUpPanelLayout,
            recyclerView: RecyclerView,
            lottieAnimationView: LottieAnimationView,
            huaweiMap: HuaweiMap
        )
        fun setBookmarkListRecent(
            context: Context,
            activity: MainActivity,
            textView: TextView,
            slideUpPanel: SlidingUpPanelLayout,
            recyclerView: RecyclerView,
            lottieAnimationView: LottieAnimationView,
            huaweiMap: HuaweiMap
        )
        fun setPrefs(
            context: Context,
            linearLayout: LinearLayout,
            sharedPrefsManager: SharedPrefsManager
        )
        fun addBadge(context: Context,position:Int,bottomBar: BottomBar)
    }
}