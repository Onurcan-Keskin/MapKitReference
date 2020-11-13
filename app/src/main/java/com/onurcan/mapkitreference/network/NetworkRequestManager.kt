package com.onurcan.mapkitreference.network

import com.huawei.hms.maps.model.LatLng
import com.onurcan.mapkitreference.helper.Constants
import com.onurcan.mapkitreference.helper.showLogDebug
import com.onurcan.mapkitreference.helper.showLogInfo
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object NetworkRequestManager {
    private const val MAX_TIMES = 10
    fun getWalkingRoutePlanningResult(
        origin: LatLng, destination: LatLng,
        listener: OnNetworkListener?
    ) {
        getWalkingRoutePlanningResult(origin, destination, listener, 0, false)
    }

    private fun getWalkingRoutePlanningResult(
        origin: LatLng, destination: LatLng,
        listener: OnNetworkListener?, count: Int, needEncode: Boolean
    ) {
        var count = count
        val curCount = ++count
        showLogInfo(Constants.mNetworkRequestManagerTag, "current count: $curCount")
        Thread(Runnable {
            val response =
                NetClient.netClient!!.getWalkingRoutePlanningResult(origin, destination, needEncode)
            var result = ""
            var returnCode = ""
            var returnDesc = ""
            var need = needEncode
            try {
                result = response!!.body()!!.string()
                val jsonObject = JSONObject(result)
                returnCode = jsonObject.optString("returnCode")
                returnDesc = jsonObject.optString("returnDesc")
            } catch (e: NullPointerException) {
                returnDesc = "Request Fail!"
            } catch (e: IOException) {
                returnDesc = "Request Fail!"
            } catch (e: JSONException) {
            }
            if (returnCode == "0") {
                listener?.requestSuccess(result)
                return@Runnable
            }
            if (curCount >= MAX_TIMES) {
                listener?.requestFail(returnDesc)
                return@Runnable
            }
            if (returnCode == "6") {
                need = true
            }
            getWalkingRoutePlanningResult(origin, destination, listener, curCount, need)
        }).start()
    }

    fun getBicyclingRoutePlanningResult(
        origin: LatLng, destination: LatLng,
        listener: OnNetworkListener?
    ) {
        getBicyclingRoutePlanningResult(origin, destination, listener, 0, false)
    }

    private fun getBicyclingRoutePlanningResult(
        origin: LatLng, destination: LatLng,
        listener: OnNetworkListener?, count: Int, needEncode: Boolean
    ) {
        var count = count
        val curCount = ++count
        showLogInfo(Constants.mNetworkRequestManagerTag, "current count: $curCount")
        Thread(Runnable {
            val response = NetClient.netClient!!
                .getBicyclingRoutePlanningResult(origin, destination, needEncode)
            if (response?.body() != null && response.isSuccessful) {
                try {
                    val result: String = response.body()!!.string()
                    listener?.requestSuccess(result)
                    return@Runnable
                } catch (e: IOException) {
                }
            }
            var returnCode = ""
            var returnDesc = ""
            var need = needEncode
            try {
                val result: String = response!!.body()!!.string()
                val jsonObject = JSONObject(result)
                returnCode = jsonObject.optString("returnCode")
                returnDesc = jsonObject.optString("returnDesc")
            } catch (e: NullPointerException) {
                returnDesc = "Request Fail!"
            } catch (e: IOException) {
                returnDesc = "Request Fail!"
            } catch (e: JSONException) {
            }
            if (curCount >= MAX_TIMES) {
                listener?.requestFail(returnDesc)
                return@Runnable
            }
            if (returnCode == "6") {
                need = true
            }
            getBicyclingRoutePlanningResult(origin, destination, listener, curCount, need)
        }).start()
    }

    fun getDrivingRoutePlanningResult(
        latLng1: LatLng, latLng2: LatLng,
        listener: OnNetworkListener?
    ) {
        getDrivingRoutePlanningResult(latLng1, latLng2, listener, 0, false)
    }

    private fun getDrivingRoutePlanningResult(
        origin: LatLng, destination: LatLng,
        listener: OnNetworkListener?, count: Int, needEncode: Boolean
    ) {
        var count = count
        val curCount = ++count
        showLogDebug(Constants.mNetworkRequestManagerTag, "current count: $curCount")
        Thread(Runnable {
            val response =
                NetClient.netClient!!.getDrivingRoutePlanningResult(origin, destination, needEncode)
            if (response?.body() != null && response.isSuccessful) {
                try {
                    val result: String = response.body()!!.string()
                    listener?.requestSuccess(result)
                    return@Runnable
                } catch (e: IOException) {
                }
            }
            var returnCode = ""
            var returnDesc = ""
            var need = needEncode
            try {
                val result: String = response!!.body()!!.string()
                val jsonObject = JSONObject(result)
                returnCode = jsonObject.optString("returnCode")
                returnDesc = jsonObject.optString("returnDesc")
            } catch (e: NullPointerException) {
                returnDesc = "Request Fail!"
            } catch (e: IOException) {
                returnDesc = "Request Fail!"
            } catch (e: JSONException) {
            }
            if (curCount >= MAX_TIMES) {
                listener?.requestFail(returnDesc)
                return@Runnable
            }
            if (returnCode == "6") {
                need = true
            }
            getDrivingRoutePlanningResult(origin, destination, listener, curCount, need)
        }).start()
    }

    interface OnNetworkListener {
        fun requestSuccess(result: String?)
        fun requestFail(errorMsg: String?)
    }
}