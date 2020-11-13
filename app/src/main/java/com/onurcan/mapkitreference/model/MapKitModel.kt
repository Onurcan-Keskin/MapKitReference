package com.onurcan.mapkitreference.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import com.huawei.hms.site.widget.SearchFilter
import com.huawei.hms.site.widget.SearchIntent
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.databinding.ActivityMainBinding
import com.onurcan.mapkitreference.helper.*
import com.onurcan.mapkitreference.ui.activities.MainActivity
import com.onurcan.mapkitreference.ui.contracts.MapKitBIContract
import com.onurcan.mapkitreference.ui.presenters.MapKitAMPresenter
import com.onurcan.mapkitreference.ui.presenters.MapKitBIPresenter
import com.onurcan.mapkitreference.ui.presenters.MapKitBPresenter
import com.onurcan.mapkitreference.ui.presenters.MapKitMainPresenter
import com.onurcan.mapkitreference.view.IMapHelper
import java.net.URLEncoder

class MapKitModel constructor(context: MainActivity) : IMapHelper, OnMapReadyCallback,
    MapKitBIContract.ViewBI {

    private var hMap: HuaweiMap? = null

    private var cntx = context

    private var markerList: MutableList<Marker> = ArrayList()

    /* Map Layout Elements*/
    private var binding: ActivityMainBinding

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null

    private lateinit var currentPolyline: Polyline

    private var mLat: Double = 0.0
    private var mLong: Double = 0.0

    private var sharedPrefsManager: SharedPrefsManager

    private var mLocationList: MutableList<String> = ArrayList()

    private var searchIntent: SearchIntent? = null

    private val mapKitMainPresenter: MapKitMainPresenter by lazy {
        MapKitMainPresenter()
    }

    private val mapKitBIPresenter: MapKitBIPresenter by lazy {
        MapKitBIPresenter()
    }

    private val mapKitAMPresenter: MapKitAMPresenter by lazy {
        MapKitAMPresenter()
    }

    private val mapKitBPresenter: MapKitBPresenter by lazy {
        MapKitBPresenter()
    }

    init {
        binding = ActivityMainBinding.inflate(cntx.layoutInflater)
        val view = binding.root
        cntx.setContentView(view)
        sharedPrefsManager = SharedPrefsManager(cntx)
    }

    override fun onSavedInstanceBundle(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.mapViewBundleKey)
        }
        binding.mapLayout.mapMap.onCreate(mapViewBundle)
        binding.mapLayout.mapMap.getMapAsync(this)
    }

    override fun onStartMap() {
        binding.mapLayout.mapMap.onStart()
    }

    override fun onPauseMap() {
        binding.mapLayout.mapMap.onPause()
    }

    override fun onResumeMap() {
        binding.mapLayout.mapMap.onResume()
    }

    override fun onDestroyMap() {
        binding.mapLayout.mapMap.onDestroy()
    }

    override fun onLowMemoryMap() {
        binding.mapLayout.mapMap.onLowMemory()
    }

    override fun ssCallback(id: String, push: String) {

    }

    override fun gotoCurrentLocation() {
        if (mLocationList.size > 0) {
            mLat = mLocationList[0].toDouble()
            mLong = mLocationList[1].toDouble()
        } else {
            mLat = Constants.mRNDLat
            mLong = Constants.mRNDLng
        }

        val cameraPosition = CameraPosition.builder()
            .target(LatLng(mLat, mLong))
            .zoom(10F)
            //.bearing(31.5F)
            .tilt(2.2F)
            .build()

        showLogDebug("Location", "lat: $mLat long: $mLong")

        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap!!.animateCamera(cu)
        //hMap!!.animateCamera(CameraUpdateFactory.newLatLng(lng))
    }

    /* From Location Kit */
    override fun fusedServices(context: Context, activity: Activity) {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context.applicationContext)
        val settingsClient = LocationServices.getSettingsClient(context.applicationContext)
        val builder = LocationSettingsRequest.Builder()
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 2500
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (null == mLocationCallback) {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val locations = locationResult.locations
                    if (locations.isNotEmpty()) {
                        for (location: Location in locations) {
                            mLat = location.latitude
                            mLong = location.longitude
                            mLocationList.add(mLat.toString())
                            mLocationList.add(mLong.toString())
                            showLogDebug(
                                Constants.mMapHelperTag,
                                "onLocationResult location[Longitude,Latitude,Accuracy]:" + mLong
                                    .toString() + "," + mLat.toString() + "," + location.accuracy
                            )
                            // gotoCurrentLocation(location.longitude,location.latitude)
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    val flag = locationAvailability.isLocationAvailable
                    showLogDebug(
                        Constants.mMapHelperTag,
                        "onLocationAvailability isLocationAvailable:$flag"
                    )
                }
            }
        }
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()
        settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener {
            fusedLocationProviderClient!!
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener {
                    // Processing when the API call is successful.
                }
                .addOnFailureListener {
                    when ((it as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                val rae = it as ResolvableApiException
                                rae.startResolutionForResult(activity, 0)
                            } catch (sie: IntentSender.SendIntentException) {

                            }
                        }
                    }
                }
        }
    }

    override fun onMapReady(map: HuaweiMap?) {
        // binding = ActivityMainBinding.inflate(cntx.layoutInflater)
        hMap = map
        hMap!!.isMyLocationEnabled = true
        hMap!!.uiSettings.isMyLocationButtonEnabled = true
        hMap!!.setOnMapLoadedCallback {
            showLogDebug(Constants.mMapHelperTag, "onMapLoaded:successful")
        }
        gotoCurrentLocation()

        /* Search Intent */
        searchIntent = SearchIntent()
        searchIntent!!.setApiKey(URLEncoder.encode(Constants.apiKey, "utf-8"))

        val customInfoWindow = CustomInfoWindowModel(cntx)
        hMap!!.setInfoWindowAdapter(customInfoWindow)

        addMarkerListenerByInteracting()

        fusedServices(cntx, cntx)

        hMap!!.setOnMapClickListener {
            mapKitMainPresenter.mapClicked(
                binding.materialEditTextLayout,
                binding.mapGo,
                binding.mapDetails,
                binding.mainBottomBar,
                binding.recyclerPoi,
                binding.distancePopIncluder.root
            )
        }

        mapKitBIPresenter.initLocation(cntx)
        mapKitBIPresenter.requestLocationUpdate()

        binding.fromToIncluder.fromToBannerIncluder.fromToBackButton.setOnClickListener {
            mapKitMainPresenter.onGoClicked(
                binding.fromToIncluder.root,
                binding.materialEditTextLayout,
                binding.recyclerPoi,
                binding.mapDetails,
                binding.distancePopIncluder.root,
                binding.mapGo
            )
        }

        binding.mapGo.setOnClickListener {
            mapKitMainPresenter.onGoClicked(
                binding.fromToIncluder.root,
                binding.materialEditTextLayout,
                binding.recyclerPoi,
                binding.mapDetails,
                binding.distancePopIncluder.root,
                binding.mapGo
            )
        }

        mapKitBPresenter.initLocation(cntx)
        mapKitBPresenter.requestLocationUpdate()
        binding.mainBottomBar.setOnTabReselectListener {
            when (it) {
                R.id.tab_explore -> mapKitAMPresenter.onExplore(
                    binding.bottomSheetAroundInc.aroundMeSlidingLayout,
                    binding.poiEditText,
                    hMap
                )
                R.id.tab_around -> {
                    binding.bottomSheetAroundInc.root.visibility = View.VISIBLE
                    mapKitAMPresenter.setAMRecycler(
                        cntx, binding.bottomSheetAroundInc.aroundMePoiTypeRecycler, mLat, mLong,
                        binding.bottomSheetAroundInc.aroundMeRecycler,
                        binding.bottomSheetAroundInc.aroundMeLottieInc.root,
                        binding.bottomSheetInc.root,
                        hMap
                    )

                    mapKitAMPresenter.onAroundMe(
                        cntx,
                        mLat,
                        mLong,
                        hMap,
                        binding.bottomSheetAroundInc.aroundMeRecycler,
                        binding.bottomSheetAroundInc.aroundMeLottieInc.root,
                        binding.bottomSheetAroundInc.aroundMeSlidingLayout,
                        mapKitAMPresenter.getAroundMeTextUItem(binding.bottomSheetInc.poiBanner.poiName)
                    )

                    mapKitAMPresenter.listenSliderState(binding.bottomSheetAroundInc.aroundMeSlidingLayout)
                }
                R.id.tab_saved -> {
                    binding.bottomSheetBookmarkInc.root.visibility = View.VISIBLE
                    mapKitBPresenter.setBookmarkListAll(
                        cntx,
                        cntx,
                        binding.bottomSheetBookmarkInc.allText,
                        binding.bottomSheetBookmarkInc.root,
                        binding.bottomSheetBookmarkInc.bookmarkRecyclerAll,
                        binding.bottomSheetBookmarkInc.allLottie,
                        binding.bottomSheetBookmarkInc.lottieText,
                        binding.bottomSheetBookmarkInc.dragView,
                        sharedPrefsManager,
                        hMap!!
                    )
                    mapKitBPresenter.setBookmarkListNearby(
                        cntx,
                        cntx,
                        binding.bottomSheetBookmarkInc.nearbyText,
                        binding.bottomSheetBookmarkInc.root,
                        binding.bottomSheetBookmarkInc.bookmarkRecyclerNearby,
                        binding.bottomSheetBookmarkInc.nearbyLottie,
                        hMap!!
                    )
                    mapKitBPresenter.setBookmarkListRecent(
                        cntx,
                        cntx,
                        binding.bottomSheetBookmarkInc.recentText,
                        binding.bottomSheetBookmarkInc.root,
                        binding.bottomSheetBookmarkInc.bookmarkRecyclerRecent,
                        binding.bottomSheetBookmarkInc.recentLottie,
                        hMap!!)
                    mapKitBPresenter.listenSliderState(binding.bottomSheetBookmarkInc.root)
                    mapKitBPresenter.addBadge(cntx, 1, binding.mainBottomBar)
                }
            }
        }

        mapKitMainPresenter.setPrefs(
            sharedPrefsManager, cntx,
            binding.bottomSheetInc.scrollable,
            binding.bottomSheetAroundInc.dragView,
            binding.mainBottomBar,
            hMap
        )

        showLogInfo("searchPOI", "$mLat,$mLong")
        showLogInfo(
            Constants.mMapHelperTag,
            "Panel_State: " + "${binding.bottomSheetInc.slidingLayout.panelState}"
        )

        /* Main Poi Recycler */
        mapKitMainPresenter.setMainPoiRecycler(
            cntx,
            binding.recyclerPoi,
            mLat,
            mLong,
            hMap,
            sharedPrefsManager
        )

        binding.mapDetails.setOnClickListener {
            if (sharedPrefsManager.loadNightModeState())
                mapKitMainPresenter.setDarkPopUp(cntx, binding.mapDetails, hMap!!)
            else
                mapKitMainPresenter.setLightPopUp(cntx, binding.mapDetails, hMap!!)
        }

    }

    override fun addMarkerListenerByInteracting() {
        if (null == hMap) return

        hMap!!.setOnMarkerDragListener(object : HuaweiMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker?) {
                showLogInfo(Constants.mMapHelperTag, "onMarkerDragStart: ")
                marker!!.hideInfoWindow()
            }

            override fun onMarkerDrag(marker: Marker?) {
                val lat = marker!!.position.latitude
                val long = marker.position.longitude

                marker.title = mapKitMainPresenter.setPinTitle(lat, long, cntx)
            }

            override fun onMarkerDragEnd(marker: Marker?) {
                val lat = marker!!.position.latitude
                val long = marker.position.longitude
                marker.showInfoWindow()
                marker.title = mapKitMainPresenter.setPinTitle(lat, long, cntx)
            }
        })
        hMap!!.setOnInfoWindowClickListener { marker ->
            marker.showInfoWindow()
            val lat = marker.position.latitude
            val long = marker.position.longitude
            binding.poiEditText.setText(marker.title)
            mapKitBIPresenter.listenBottomSheetImageState(
                binding.bottomSheetInc.slidingLayout,
                binding.bottomSheetInc.poiBanner.poiImage
            )

            binding.bottomSheetInc.bottomSheetUi.uiSave.setOnClickListener {

                mapKitMainPresenter.openList(
                    cntx,
                    cntx,
                    R.style.DialogSlide,
                    sharedPrefsManager,
                    marker
                )
            }

            mapKitMainPresenter.showSheetFromPlacedMarker(
                cntx,
                binding.bottomSheetInc.slidingLayout,
                lat,
                long,
                binding.bottomSheetInc.poiBanner.poiName,
                binding.bottomSheetInc.bottomSheetPoiInfo.poiAddress,
                binding.bottomSheetInc.bottomSheetPoiInfo.bottomPoiAddressItem,
                binding.bottomSheetInc.bottomSheetPoiInfo.poiHours,
                marker
            )

            binding.bottomSheetInc.bottomSheetPoiInfo.gotoGMaps.setOnClickListener {
                mapKitBIPresenter.openWithGoogle(cntx, marker)
            }

            binding.bottomSheetInc.bottomSheetPoiInfo.gotoHMaps.setOnClickListener {
                mapKitBIPresenter.openWithHere(cntx, marker)
            }

            binding.bottomSheetInc.bottomSheetPoiInfo.gotoOMaps.setOnClickListener {
                mapKitBIPresenter.openWithOpen(cntx, marker)
            }

            binding.bottomSheetInc.bottomSheetUi.uiShare.setOnClickListener {
                mapKitBIPresenter.startShareDialog(
                    cntx,
                    marker,
                    sharedPrefsManager,
                    R.style.DialogSlide
                )
            }

            binding.distancePopIncluder.mcView.setOnClickListener {
                binding.goByPopIncluder.root.expandView()
                if (binding.goByPopIncluder.root.visibility == View.VISIBLE) {
                    binding.distancePopIncluder.imArrow.setImageResource(R.drawable.ic_arrow_up)
                    binding.goByPopIncluder.goByCar.setOnClickListener {
                        mapKitBIPresenter.requestGetDirectionDriving(
                            cntx,
                            cntx,
                            LatLng(
                                marker.position.latitude,
                                marker.position.longitude
                            ),
                            binding.bottomSheetInc.poiBanner.poiEta,
                            binding.distancePopIncluder.tvDistance,
                            binding.distancePopIncluder.root,
                            hMap
                        )
                    }
                    binding.goByPopIncluder.goByBike.setOnClickListener {
                        binding.bottomSheetInc.root.visibility = View.GONE
                        mapKitBIPresenter.requestGetDirectionBicycling(
                            cntx,
                            cntx,
                            LatLng(
                                marker.position.latitude,
                                marker.position.longitude
                            ),
                            binding.bottomSheetInc.poiBanner.poiEta,
                            binding.distancePopIncluder.tvDistance,
                            binding.distancePopIncluder.root,
                            hMap
                        )
                    }
                    binding.goByPopIncluder.goByWalk.setOnClickListener {
                        mapKitBIPresenter.requestGetDirectionWalking(
                            cntx,
                            cntx,
                            LatLng(
                                marker.position.latitude,
                                marker.position.longitude
                            ),
                            binding.bottomSheetInc.poiBanner.poiEta,
                            binding.distancePopIncluder.tvDistance,
                            binding.distancePopIncluder.root,
                            hMap
                        )
                    }
                } else {
                    binding.distancePopIncluder.imArrow.setImageResource(R.drawable.ic_arrow_down)
                }
            }

            binding.bottomSheetInc.poiBar.setOnTabReselectListener {
                when (it) {
                    R.id.by_car -> {
                        /* By Car */
                        showToast(cntx, "By Car")
                        binding.bottomSheetInc.bottomSheetUi.uiDirections.setOnClickListener {
                            binding.bottomSheetInc.root.visibility = View.GONE
                            mapKitBIPresenter.requestGetDirectionDriving(
                                cntx,
                                cntx,
                                LatLng(
                                    marker.position.latitude,
                                    marker.position.longitude
                                ),
                                binding.bottomSheetInc.poiBanner.poiEta,
                                binding.distancePopIncluder.tvDistance,
                                binding.distancePopIncluder.root,
                                hMap
                            )
                        }
                    }
                    R.id.by_bike -> {
                        /* By Cycle */
                        showToast(cntx, "By Bike")
                        binding.bottomSheetInc.bottomSheetUi.uiDirections.setOnClickListener {
                            binding.bottomSheetInc.root.visibility = View.GONE
                            mapKitBIPresenter.requestGetDirectionBicycling(
                                cntx,
                                cntx,
                                LatLng(
                                    marker.position.latitude,
                                    marker.position.longitude
                                ),
                                binding.bottomSheetInc.poiBanner.poiEta,
                                binding.distancePopIncluder.tvDistance,
                                binding.distancePopIncluder.root,
                                hMap
                            )
                        }
                    }
                    R.id.by_walk -> {
                        /* By Walking */
                        showToast(cntx, "By Walk")
                        binding.bottomSheetInc.bottomSheetUi.uiDirections.setOnClickListener {
                            binding.bottomSheetInc.root.visibility = View.GONE
                            mapKitBIPresenter.requestGetDirectionWalking(
                                cntx,
                                cntx,
                                LatLng(
                                    marker.position.latitude,
                                    marker.position.longitude
                                ),
                                binding.bottomSheetInc.poiBanner.poiEta,
                                binding.distancePopIncluder.tvDistance,
                                binding.distancePopIncluder.root,
                                hMap
                            )
                        }
                    }
                }
            }

            //mapKitMainPresenter.getAddressFromPin(lat, long, cntx)
        }
        hMap!!.setOnInfoWindowCloseListener { marker ->
            showLogDebug(Constants.mMapHelperTag, "infowindowclose")
            marker.hideInfoWindow()
            binding.poiEditText.setText("")
            mapKitMainPresenter.uiVisible(
                binding.materialEditTextLayout,
                binding.recyclerPoi,
                binding.mapGo,
                binding.mapDetails
            )

        }
        hMap!!.setOnInfoWindowLongClickListener {
            it.remove()
            mapKitBIPresenter.removePolylines()
            binding.distancePopIncluder.tvDistance.text = ""
            binding.poiEditText.setText("")
        }

        hMap!!.setOnMapLongClickListener {
            val lat = it.latitude
            val long = it.longitude
            val mMarker = hMap!!.addMarker(
                MarkerOptions().position(it)
                    .title(mapKitMainPresenter.setPinTitle(lat, long, cntx))
                    .clusterable(true).draggable(true)
            )
            binding.poiEditText.setText(mMarker.title)
            markerList.add(mMarker)
        }
    }

    override fun getDirectionSuccess(pathList: ArrayList<LatLng>) {
        currentPolyline = hMap!!.addPolyline(
            PolylineOptions().addAll(pathList)
                .color(R.color.huawei_red)
        )
    }

    override fun getDirectionFailure(message: String) {
        showSnackbar(binding.root, cntx.getString(R.string.error_polyline))
    }

    override fun onSiteResult(requestCode: Int, resultCode: Int, data: Intent?, controller: Int) {
        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            showToast(cntx, "Site Controller")
            hMap!!.clear()
            if (SearchIntent.isSuccess(resultCode)) {
                val site = searchIntent!!.getSiteFromIntent(data)

                binding.bottomSheetInc.slidingLayout.visibility = View.VISIBLE
                mapKitBIPresenter.listenBottomSheetImageState(
                    binding.bottomSheetInc.slidingLayout,
                    binding.bottomSheetInc.poiBanner.poiImage
                )

                mapKitAMPresenter.fillSheet(
                    binding.bottomSheetInc.poiBanner.poiName,
                    binding.bottomSheetInc.poiBanner.poiType,
                    binding.bottomSheetInc.poiBanner.poiEta,
                    binding.bottomSheetInc.bottomSheetPoiInfo.poiAddress,
                    binding.bottomSheetInc.bottomSheetPoiInfo.poiHours,
                    binding.bottomSheetInc.bottomSheetPoiInfo.poiWebsite,
                    site,
                    cntx
                )

                binding.poiEditText.setText(site.name)

                showLogDebug(
                    Constants.mMapHelperTag,
                    "Panel State: " + "${binding.bottomSheetInc.slidingLayout.panelState}"
                )

                mapKitMainPresenter.addMarker(
                    hMap!!,
                    LatLng(site.location.lat, site.location.lng),
                    site.name
                )
                mapKitMainPresenter.moveCamera(
                    hMap!!,
                    LatLng(site.location.lat, site.location.lng)
                )
                showToast(cntx, site.name)
            } else
                binding.bottomSheetInc.slidingLayout.visibility = View.GONE
        }
    }

    override fun onSiteStartLocation(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        controller: Int
    ) {
        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            showToast(cntx, "Start Controller")
            hMap!!.clear()
            if (SearchIntent.isSuccess(resultCode)) {
                val site = searchIntent!!.getSiteFromIntent(data)
                binding.fromToIncluder.fromToBannerIncluder.fromToYourLocation.setText(site.name)

                mapKitMainPresenter.addMarker(
                    hMap!!,
                    LatLng(site.location.lat, site.location.lng),
                    site.name
                )
                mapKitMainPresenter.moveCamera(
                    hMap!!,
                    LatLng(site.location.lat, site.location.lng)
                )
            }
        }
    }

    override fun onSiteDestinationLocation(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        controller: Int
    ) {
        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            showToast(cntx, "Destination Controller")
            hMap!!.clear()
            if (SearchIntent.isSuccess(resultCode)) {
                val site = searchIntent!!.getSiteFromIntent(data)
                binding.fromToIncluder.fromToBannerIncluder.fromToDestinationLocation.setText(
                    site.name
                )

                mapKitMainPresenter.addMarker(
                    hMap!!,
                    LatLng(site.location.lat, site.location.lng),
                    site.name
                )
                mapKitMainPresenter.moveCamera(
                    hMap!!,
                    LatLng(site.location.lat, site.location.lng)
                )
            }
        }
    }

    override fun checkByControllerSite(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        controller: Int
    ) {
        when (controller) {

            Constants.mRequestSiteLocation -> {
                onSiteResult(requestCode, resultCode, data, controller)
            }

            Constants.mRequestStartLocation -> {
                onSiteStartLocation(requestCode, resultCode, data, controller)
            }

            Constants.mRequestDestinationLocation -> {
                onSiteDestinationLocation(requestCode, resultCode, data, controller)
            }
        }
    }

    override fun onKeyboardSearchSite(constant: Int) {
        if (constant == Constants.mRequestSiteLocation) {
            val searchFilter = SearchFilter()
            searchFilter.query = binding.poiEditText.text.toString()
            searchFilter.radius = 300
            searchFilter.countryCode = "TR"
            searchFilter.language = "tr"
            searchIntent!!.setSearchFilter(searchFilter)

            val intent = searchIntent!!.getIntent(cntx)
            cntx.startActivityForResult(intent, SearchIntent.SEARCH_REQUEST_CODE)
        }
    }

    override fun onKeyboardSearchStartSite(constant: Int) {
        if (constant == Constants.mRequestStartLocation) {
            val searchFilter = SearchFilter()
            searchFilter.query =
                binding.fromToIncluder.fromToBannerIncluder.fromToYourLocation.text.toString()
            searchFilter.radius = 300
            searchFilter.countryCode = "TR"
            searchFilter.language = "tr"
            searchIntent!!.setSearchFilter(searchFilter)

            val intent = searchIntent!!.getIntent(cntx)
            cntx.startActivityForResult(intent, SearchIntent.SEARCH_REQUEST_CODE)
        }
    }

    override fun onKeyboardSearchDestinationSite(constant: Int) {
        if (constant == Constants.mRequestDestinationLocation) {
            val searchFilter = SearchFilter()
            searchFilter.query =
                binding.fromToIncluder.fromToBannerIncluder.fromToYourLocation.text.toString()
            searchFilter.radius = 300
            searchFilter.countryCode = "TR"
            searchFilter.language = "tr"
            searchIntent!!.setSearchFilter(searchFilter)

            val intent = searchIntent!!.getIntent(cntx)
            cntx.startActivityForResult(intent, SearchIntent.SEARCH_REQUEST_CODE)
        }
    }

}