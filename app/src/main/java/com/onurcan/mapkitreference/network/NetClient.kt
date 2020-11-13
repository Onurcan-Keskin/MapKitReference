package com.onurcan.mapkitreference.network

import com.huawei.hms.maps.model.LatLng
import com.onurcan.mapkitreference.helper.Constants
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class NetClient private constructor() {

    private fun initOkHttpClient(): OkHttpClient? {
        if (client == null) {
            client = OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS) // Set the read timeout.
                .connectTimeout(10000, TimeUnit.MILLISECONDS) // Set the connect timeout.
                .build()
        }
        return client
    }

    fun getWalkingRoutePlanningResult(
        latLng1: LatLng,
        latLng2: LatLng,
        needEncode: Boolean
    ): Response? {
        var key = Constants.apiKey
        if (needEncode) {
            try {
                key = URLEncoder.encode(Constants.apiKey, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        val url = "${Constants.mRouteWalkingUrl}?key=$key"
        var response: Response? = null
        val origin = JSONObject()
        val destination = JSONObject()
        val json = JSONObject()
        try {
            origin.put("lat", latLng1.latitude)
            origin.put("lng", latLng1.longitude)
            destination.put("lat", latLng2.latitude)
            destination.put("lng", latLng2.longitude)
            json.put("origin", origin)
            json.put("destination", destination)
            val requestBody = RequestBody.create(JSON, json.toString())
            val request = Request.Builder().url(url).post(requestBody).build()
            response = netClient!!.initOkHttpClient()!!.newCall(request).execute()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    fun getBicyclingRoutePlanningResult(
        latLng1: LatLng,
        latLng2: LatLng,
        needEncode: Boolean
    ): Response? {
        var key = Constants.apiKey
        if (needEncode) {
            try {
                key = URLEncoder.encode(Constants.apiKey, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        val url = "${Constants.mRouteBicyclingUrl}?key=$key"
        var response: Response? = null
        val origin = JSONObject()
        val destination = JSONObject()
        val json = JSONObject()
        try {
            origin.put("lat", latLng1.latitude)
            origin.put("lng", latLng1.longitude)
            destination.put("lat", latLng2.latitude)
            destination.put("lng", latLng2.longitude)
            json.put("origin", origin)
            json.put("destination", destination)
            val requestBody = RequestBody.create(JSON, json.toString())
            val request = Request.Builder().url(url).post(requestBody).build()
            response = netClient!!.initOkHttpClient()!!.newCall(request).execute()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    fun getDrivingRoutePlanningResult(
        latLng1: LatLng,
        latLng2: LatLng,
        needEncode: Boolean
    ): Response? {
        var key = Constants.apiKey
        if (needEncode) {
            try {
                key = URLEncoder.encode(Constants.apiKey, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        val url = "${Constants.mRouteDrivingUrl}?key=$key"
        var response: Response? = null
        val origin = JSONObject()
        val destination = JSONObject()
        val json = JSONObject()
        try {
            origin.put("lat", latLng1.latitude)
            origin.put("lng", latLng1.longitude)
            destination.put("lat", latLng2.latitude)
            destination.put("lng", latLng2.longitude)
            json.put("origin", origin)
            json.put("destination", destination)
            val requestBody = RequestBody.create(JSON, json.toString())
            val request = Request.Builder().url(url).post(requestBody).build()
            response = netClient!!.initOkHttpClient()!!.newCall(request).execute()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    companion object {
        var netClient: NetClient? = null
            get() {
                if (field == null) {
                    field = NetClient()
                }
                return field
            }
            private set
        private var client: OkHttpClient? = null
        private val JSON = MediaType.parse("application/json; charset=utf-8")
    }

    init {
        client = initOkHttpClient()
    }
}