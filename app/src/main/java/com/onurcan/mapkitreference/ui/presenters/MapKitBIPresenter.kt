package com.onurcan.mapkitreference.ui.presenters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.*
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.helper.*
import com.onurcan.mapkitreference.network.NetworkRequestManager
import com.onurcan.mapkitreference.ui.activities.WebViewActivity
import com.onurcan.mapkitreference.ui.contracts.MapKitBIContract
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("HandlerLeak")
class MapKitBIPresenter : MapKitBIContract.PresenterBI,
    MapKitBIContract.PresenterBIEX {

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private lateinit var settingsClient: SettingsClient
    private var mLatLngBounds: LatLngBounds? = null

    private val mPolylines: MutableList<Polyline> = ArrayList()
    private val mPaths: MutableList<List<LatLng>> = ArrayList()
    private var mMarkerOrigin: Marker? = null
    private var mMarkerDestination: Marker? = null

    /* Individual Dropped Pin Listener State */
    /**
     * @param slideUpPanel is EXPANDED
     * show:
     * @return imageView
     * */
    fun listenBottomSheetImageState(slideUpPanel: SlidingUpPanelLayout, imageView: ImageView) {
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
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: COLLAPSED")
                        slideUpPanel.visibility = View.GONE
                        imageView.visibility = View.GONE
                    }
                    SlidingUpPanelLayout.PanelState.EXPANDED -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: EXPANDED")
                        imageView.visibility = View.VISIBLE
                        Picasso.get().load(R.drawable.default_poi_image).into(imageView)
                    }
                    SlidingUpPanelLayout.PanelState.ANCHORED -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: ANCHORED")
                    }
                    SlidingUpPanelLayout.PanelState.DRAGGING -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: DRAGGING")
                    }
                    SlidingUpPanelLayout.PanelState.HIDDEN -> {
                        showLogDebug(Constants.mMapHelperPresenterTag, "PanelState: HIDDEN")
                    }
                }
            }
        })
    }

    override fun requestGetDirectionWalking(
        context: Context,
        activity: Activity,
        destination: LatLng,
        poiEta: TextView,
        tvDistance: TextView,
        tvHolder: ConstraintLayout,
        hMap: HuaweiMap?
    ) {
        removePolylines()
        if (::currentLocation.isInitialized) {
            NetworkRequestManager.getWalkingRoutePlanningResult(
                LatLng(currentLocation.latitude, currentLocation.longitude),
                LatLng(destination.latitude, destination.longitude),
                object : NetworkRequestManager.OnNetworkListener {
                    override fun requestSuccess(result: String?) {
                        generateRoute(
                            result!!,
                            context,
                            activity,
                            poiEta,
                            tvDistance,
                            tvHolder,
                            hMap
                        )
                    }

                    override fun requestFail(errorMsg: String?) {
                        showLogError(Constants.mMapHelperPresenterTag, errorMsg.toString())
                    }
                })
        } else {
            showToast(context, context.getString(R.string.error_location))
        }
    }

    override fun requestGetDirectionBicycling(
        context: Context,
        activity: Activity,
        destination: LatLng,
        poiEta: TextView,
        tvDistance: TextView,
        tvHolder: ConstraintLayout,
        hMap: HuaweiMap?
    ) {
        removePolylines()
        if (::currentLocation.isInitialized) {
            NetworkRequestManager.getBicyclingRoutePlanningResult(
                LatLng(currentLocation.latitude, currentLocation.longitude),
                LatLng(destination.latitude, destination.longitude),
                object : NetworkRequestManager.OnNetworkListener {
                    override fun requestSuccess(result: String?) {
                        generateRoute(
                            result!!,
                            context,
                            activity,
                            poiEta,
                            tvDistance,
                            tvHolder,
                            hMap
                        )
                    }

                    override fun requestFail(errorMsg: String?) {
                        showLogError(Constants.mMapHelperPresenterTag, errorMsg.toString())
                    }
                })
        } else {
            showToast(context, context.getString(R.string.error_location))
        }
    }

    override fun requestGetDirectionDriving(
        context: Context,
        activity: Activity,
        destination: LatLng,
        poiEta: TextView,
        tvDistance: TextView,
        tvHolder: ConstraintLayout,
        hMap: HuaweiMap?
    ) {
        removePolylines()
        if (::currentLocation.isInitialized) {
            NetworkRequestManager.getDrivingRoutePlanningResult(
                LatLng(currentLocation.latitude, currentLocation.longitude),
                LatLng(destination.latitude, destination.longitude),
                object : NetworkRequestManager.OnNetworkListener {
                    override fun requestSuccess(result: String?) {
                        generateRoute(
                            result!!,
                            context,
                            activity,
                            poiEta,
                            tvDistance,
                            tvHolder,
                            hMap
                        )
                    }

                    override fun requestFail(errorMsg: String?) {
                        showLogError(Constants.mMapHelperPresenterTag, errorMsg.toString())
                    }
                })
        } else {
            showToast(context, context.getString(R.string.error_location))
        }
    }

    override fun generateRoute(
        json: String,
        context: Context,
        activity: Activity,
        poiEta: TextView,
        tvDistance: TextView,
        tvHolder: ConstraintLayout,
        hMap: HuaweiMap?
    ) {
        try {
            val jsonObject = JSONObject(json)
            val routes = jsonObject.optJSONArray("routes")
            if (null == routes || routes.length() == 0) {
                return
            }
            val route = routes.getJSONObject(0)
            val bounds = route.optJSONObject("bounds")
            if (null != bounds && bounds.has("southwest") && bounds.has("northeast")) {
                val southwest = bounds.optJSONObject("southwest")
                val northeast = bounds.optJSONObject("northeast")
                val sw = LatLng(southwest.optDouble("lat"), southwest.optDouble("lng"))
                val ne = LatLng(northeast.optDouble("lat"), northeast.optDouble("lng"))
                mLatLngBounds = LatLngBounds(sw, ne)
            }

            // get paths
            val paths = route.optJSONArray("paths")
            for (i in 0 until paths.length()) {
                val path = paths.optJSONObject(i)
                val mPath: MutableList<LatLng> = ArrayList()
                val steps = path.optJSONArray("steps")
                val dest = paths.getJSONObject(0)
                val mDest = dest.optString("durationText")
                activity.runOnUiThread {
                    poiEta.text = mDest
                    setDistanceUI(mDest, tvDistance, tvHolder)
                    showLogDebug(Constants.mMapHelperPresenterTag, "durationText $mDest")
                }
                for (j in 0 until steps.length()) {
                    val step = steps.optJSONObject(j)
                    val polyline = step.optJSONArray("polyline")
                    for (k in 0 until polyline.length()) {
                        if (j > 0 && k == 0) {
                            continue
                        }
                        val line = polyline.getJSONObject(k)
                        val lat = line.optDouble("lat")
                        val lng = line.optDouble("lng")
                        val latLng = LatLng(lat, lng)
                        mPath.add(latLng)
                    }
                }
                mPaths.add(i, mPath)
            }
            renderRoute(context, mPaths, mLatLngBounds, hMap)
        } catch (e: JSONException) {
            showLogError(Constants.mMapHelperPresenterTag, "JSONException$e")
        }
    }

    override fun renderRoute(
        context: Context,
        paths: List<List<LatLng>>?,
        latLngBounds: LatLngBounds?,
        hMap: HuaweiMap?
    ) {
        if (null == paths || paths.isEmpty() || paths[0].isEmpty()) {
            showLogError(Constants.mMapHelperPresenterTag, "renderRoute: Path is null")
            return
        }
        for (i in paths.indices) {
            val path = paths[i]
            val options = PolylineOptions().color(context.getColor(R.color.huawei_red)).width(5f)
            for (latLng in path) {
                options.add(latLng)
            }
            val polyline: Polyline = hMap!!.addPolyline(options)
            mPolylines.add(i, polyline)
        }
        addOriginMarker(context, paths[0][0], hMap)
        //addDestinationMarker(context, paths[0][paths[0].size - 1], hMap)
        if (null != latLngBounds) {
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5)
            hMap!!.moveCamera(cameraUpdate)
        } else {
            hMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(paths[0][0], 13f))
            showLogError(Constants.mMapHelperPresenterTag, "renderRoute: latLngBounds is null")
        }
    }

    override fun addOriginMarker(context: Context, latLng: LatLng, hMap: HuaweiMap?) {
        if (null != mMarkerOrigin) {
            mMarkerOrigin!!.remove()
        }
        mMarkerOrigin = hMap!!.addMarker(
            MarkerOptions().position(latLng)
                .anchor(0.5f, 0.9f)
                .title(context.getString(R.string.myLocation))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }

    override fun addDestinationMarker(context: Context, latLng: LatLng, hMap: HuaweiMap?) {
        if (null != mMarkerDestination) {
            mMarkerDestination!!.remove()
        }
        mMarkerDestination = hMap!!.addMarker(
            MarkerOptions().position(latLng).draggable(true)
                .title(setPinTitle(latLng.latitude, latLng.longitude, context))
        )
    }

    override fun setPinTitle(lat: Double, long: Double, context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, long, 1)
        //"$tst1, $tst2 - $tst3"
        return address[0].getAddressLine(0)
    }

    override fun removePolylines() {
        for (polyline in mPolylines) {
            polyline.remove()
        }
        mPolylines.clear()
        mPaths.clear()
        mLatLngBounds = null
    }

    private fun setDistanceUI(distance: String, tvDistance: TextView, tvHolder: ConstraintLayout) {
        if (mPaths.isNotEmpty() || mPolylines.isNotEmpty() || distance.isNotEmpty()) {
            tvHolder.visibility = View.VISIBLE
            tvDistance.text = distance
        } else {
            tvHolder.visibility = View.GONE
        }
    }

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

    override fun requestRemoveLocationUpdate() {
    }

    override fun getLastKnownLocation(): Location? {
        var lastLocation: Location? = null
        val task = fusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener { location ->
                lastLocation = location
                return@addOnSuccessListener
            }
            .addOnFailureListener { return@addOnFailureListener }
        return lastLocation
    }

    override fun openWithHere(context: Context, marker: Marker) {
        val uri =
            "share.here.com/l/${marker.position.latitude},${marker.position.longitude}?&z=13&t=satellite&p=no"
        context.startActivity(
            Intent(context, WebViewActivity::class.java)
                .putExtra("url", uri)
                .putExtra("place", marker.title)
        )
    }

    override fun openWithGoogle(context: Context, marker: Marker) {
        val uri =
            "www.google.com/maps/search/?api=1&query=${marker.position.latitude},${marker.position.longitude}"
        context.startActivity(
            Intent(context, WebViewActivity::class.java)
                .putExtra("url", uri)
                .putExtra("place", marker.title)
        )
    }

    override fun openWithOpen(context: Context, marker: Marker) {
        val uri =
            "openstreetmap.org/?mlat=${marker.position.latitude}&mlon=${marker.position.longitude}&zoom=12"
        context.startActivity(
            Intent(context, WebViewActivity::class.java)
                .putExtra("url", uri)
                .putExtra("place", marker.title)
        )
    }

    override fun shareFunHere(context: Context, marker: Marker) {
        val uri =
            "https://share.here.com/l/${marker.position.latitude},${marker.position.longitude}?z=13&t=satellite&p=no"
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share HiMe - Travel")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
        context.startActivity(Intent.createChooser(sharingIntent, "Share Places"))
    }

    override fun shareFunGoogle(context: Context, marker: Marker) {
        val uri =
            "https://www.google.com/maps/search/?api=1&query=${marker.position.latitude},${marker.position.longitude}"
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share HiMe - Travel")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
        context.startActivity(Intent.createChooser(sharingIntent, "Share Places"))
    }

    override fun shareFunOpen(context: Context, marker: Marker) {
        val uri =
            "https://www.openstreetmap.org/#map=16/&${marker.position.longitude}&/&${marker.position.latitude}&&layers=N"
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share HiMe - Travel")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
        context.startActivity(Intent.createChooser(sharingIntent, "Share Places"))
    }

    override fun startShareDialog(
        context: Context,
        marker: Marker,
        sharedPrefsManager: SharedPrefsManager,
        type: Int
    ) {
        val dialog = Dialog(context, R.style.BlurTheme)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(width, height)
        dialog.window!!.attributes.windowAnimations = type
        dialog.setContentView(R.layout.dialog_share_with_that_map)
        dialog.setCanceledOnTouchOutside(true)
        val shareGoogle = dialog.findViewById<LinearLayout>(R.id.share_google)
        val shareHere = dialog.findViewById<LinearLayout>(R.id.share_here)
        val shareOpen = dialog.findViewById<LinearLayout>(R.id.share_open)
        val shareHolder = dialog.findViewById<LinearLayout>(R.id.dialog_share_with_that)

        if (sharedPrefsManager.loadNightModeState())
            shareHolder.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_dark)
        else
            shareHolder.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_light)

        shareGoogle.setOnClickListener {
            shareFunGoogle(context, marker)
            dialog.dismiss()
        }

        shareHere.setOnClickListener {
            shareFunHere(context, marker)
            dialog.dismiss()
        }

        shareOpen.setOnClickListener {
            shareFunOpen(context, marker)
            dialog.dismiss()
        }

        dialog.show()
    }
}