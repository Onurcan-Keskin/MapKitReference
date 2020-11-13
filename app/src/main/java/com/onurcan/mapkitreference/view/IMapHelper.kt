package com.onurcan.mapkitreference.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.huawei.hms.maps.model.LatLng

interface IMapHelper {
    fun onSavedInstanceBundle(savedInstanceState: Bundle?) //For HereMaps
    fun onStartMap()
    fun onPauseMap()
    fun onResumeMap()
    fun onDestroyMap()
    fun onLowMemoryMap()
    fun ssCallback(id: String, push: String)
    fun gotoCurrentLocation()
    fun fusedServices(context: Context, activity: Activity)
    fun addMarkerListenerByInteracting()
    fun getDirectionSuccess(pathList: ArrayList<LatLng>)
    fun onSiteResult(requestCode: Int, resultCode: Int, data: Intent?,controller:Int)
    fun onSiteStartLocation(requestCode: Int, resultCode: Int, data: Intent?,controller:Int)
    fun onSiteDestinationLocation(requestCode: Int, resultCode: Int, data: Intent?,controller:Int)
    fun checkByControllerSite(requestCode: Int, resultCode: Int, data: Intent?,controller: Int)
    fun onKeyboardSearchSite(constant:Int)
    fun onKeyboardSearchStartSite(constant:Int)
    fun onKeyboardSearchDestinationSite(constant:Int)
}