package com.onurcan.mapkitreference.ui.presenters

import android.content.Context
import android.location.Location
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.huawei.hms.location.*
import com.huawei.hms.maps.HuaweiMap
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.helper.Constants
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.onurcan.mapkitreference.helper.expandView
import com.onurcan.mapkitreference.helper.showLogDebug
import com.onurcan.mapkitreference.model.PoiItemModel
import com.onurcan.mapkitreference.ui.activities.MainActivity
import com.onurcan.mapkitreference.ui.adapters.BookmarkNearbyAdapter
import com.onurcan.mapkitreference.ui.adapters.BookmarkRecentAdapter
import com.onurcan.mapkitreference.ui.adapters.PoiItemAdapter
import com.onurcan.mapkitreference.ui.contracts.MapKitBContract
import com.roughike.bottombar.BottomBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MapKitBPresenter : MapKitBContract.PresenterB, MapKitBContract.ViewB {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private lateinit var poiItemModel: PoiItemModel

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private lateinit var settingsClient: SettingsClient

    override fun initLocation(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        settingsClient = LocationServices.getSettingsClient(context)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                val locations = p0!!.locations
                if (locations.isNotEmpty()) {
                    for (location in locations) currentLocation = location
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                val flag = p0!!.isLocationAvailable
            }
        }
    }

    override fun requestLocationUpdate() {
        fusedLocationProviderClient
            ?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
            ?.addOnSuccessListener {
                //Processing when the API call is successful.
            }
            ?.addOnFailureListener {
                //Processing when the API call fails.
            }
    }

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

    override fun setBookmarkListAll(
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
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = PoiItemAdapter(context, activity, huaweiMap, slideUpPanel)
        recyclerView.adapter = adapter

        poiItemModel = ViewModelProvider(
            activity,
            ViewModelProvider.AndroidViewModelFactory(activity.application)
        ).get(
            PoiItemModel::class.java
        )
        poiItemModel.poiItem.observe(activity, { list ->
            if (list.isNotEmpty()) {
                lottieAnimationView.visibility = View.GONE
                notFoundText.visibility=View.GONE
                recyclerView.visibility = View.VISIBLE
                list?.let { adapter.setItem(it) }
                textView.setOnClickListener {
                    setExpandedRecycler(recyclerView, textView)
                }
            } else {
                lottieAnimationView.visibility = View.VISIBLE
                notFoundText.visibility=View.VISIBLE
                recyclerView.visibility = View.GONE
                textView.setOnClickListener {
                    setExpandedLottie(lottieAnimationView, textView)
                }
            }
        })

        setPrefs(
            context,
            linearLayout,
            sharedPrefsManager
        )
    }

    override fun setBookmarkListNearby(
        context: Context,
        activity: MainActivity,
        textView: TextView,
        slideUpPanel: SlidingUpPanelLayout,
        recyclerView: RecyclerView,
        lottieAnimationView: LottieAnimationView,
        huaweiMap: HuaweiMap
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (::currentLocation.isInitialized) {
            val adapter = BookmarkNearbyAdapter(
                context,
                currentLocation.latitude,
                currentLocation.longitude,
                huaweiMap,
                recyclerView,
                slideUpPanel
            )
            recyclerView.adapter = adapter
            if (recyclerView.height == 0) {
                recyclerView.visibility = View.GONE
                lottieAnimationView.visibility = View.VISIBLE
                textView.setOnClickListener {
                    setExpandedLottie(lottieAnimationView, textView)
                }
            } else {
                recyclerView.visibility = View.VISIBLE
                lottieAnimationView.visibility = View.GONE
                textView.setOnClickListener {
                    setExpandedRecycler(recyclerView, textView)
                }
            }

            poiItemModel = ViewModelProvider(
                activity,
                ViewModelProvider.AndroidViewModelFactory(activity.application)
            ).get(
                PoiItemModel::class.java
            )

            poiItemModel.poiItem.observe(activity, { list ->
                list?.let { adapter.setItem(it) }
            })
        }
    }

    override fun setBookmarkListRecent(
        context: Context,
        activity: MainActivity,
        textView: TextView,
        slideUpPanel: SlidingUpPanelLayout,
        recyclerView: RecyclerView,
        lottieAnimationView: LottieAnimationView,
        huaweiMap: HuaweiMap
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = BookmarkRecentAdapter(
            context,
            recyclerView,
            huaweiMap,
            slideUpPanel
        )
        recyclerView.adapter = adapter
        if (recyclerView.height == 0) {
            recyclerView.visibility = View.GONE
            lottieAnimationView.visibility = View.VISIBLE
            textView.setOnClickListener {
                setExpandedLottie(lottieAnimationView, textView)
            }
        } else {
            recyclerView.visibility = View.VISIBLE
            lottieAnimationView.visibility = View.GONE
            textView.setOnClickListener {
                setExpandedRecycler(recyclerView, textView)
            }
        }

        poiItemModel = ViewModelProvider(
            activity,
            ViewModelProvider.AndroidViewModelFactory(activity.application)
        ).get(
            PoiItemModel::class.java
        )

        poiItemModel.poiItem.observe(activity, { list ->
            list?.let { adapter.setItem(it) }
        })
    }

    override fun setPrefs(
        context: Context,
        linearLayout: LinearLayout,
        sharedPrefsManager: SharedPrefsManager
    ) {
        if (sharedPrefsManager.loadNightModeState()) {
            linearLayout.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_dark)
        } else {
            linearLayout.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_light)
        }
    }

    override fun addBadge(context: Context, position: Int, bottomBar: BottomBar) {
        bottomBar.getChildAt(position)
        val badge = LayoutInflater.from(context).inflate(R.layout.badge_layout, bottomBar, false)
        val badgeLayout = FrameLayout.LayoutParams(badge?.layoutParams!!).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = context.resources.getDimension(R.dimen.elevation_sheet_bottom).toInt()
            leftMargin = context.resources.getDimension(R.dimen.badge_left_margin).toInt()
        }
        bottomBar.addView(badge, badgeLayout)
    }

    private fun setExpandedLottie(lottieAnimationView: LottieAnimationView,textView: TextView){
        lottieAnimationView.expandView()
        if (lottieAnimationView.visibility==View.GONE)
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_arrow_down,0)
        else
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_arrow_up,0)
    }

    private fun setExpandedRecycler(recyclerView: RecyclerView,textView: TextView){
        recyclerView.expandView()
        if (recyclerView.visibility==View.GONE)
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_arrow_down,0)
        else
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_arrow_up,0)
    }

}