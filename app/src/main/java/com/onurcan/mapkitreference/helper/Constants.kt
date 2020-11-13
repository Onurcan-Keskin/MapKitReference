package com.onurcan.mapkitreference.helper

class Constants {
    companion object {
        /* Map Key */
        const val mapViewBundleKey = "MapViewBundleKey"

        /* API Key */
        const val apiKey =
            "CgB6e3x9P6aIyvnBXBKt02KFlUUPSbkU7OAgD2+qESO+0k2u5GTm6notT46HNZdhX5I9cJOReUd3NY7APhirlw8C"

        /* Huawei R&D Location */
        const val mRNDLat = 41.0264686
        const val mRNDLng = 28.9980456

        /* Request from Location */
        const val mRequestSiteLocation = 1000
        const val mRequestStartLocation = 1001
        const val mRequestDestinationLocation = 1002

        /* Directions */
        const val mRouteService = "https://mapapi.cloud.huawei.com/mapApi/v1/routeService/"
        const val mRouteWalkingUrl =
            "https://mapapi.cloud.huawei.com/mapApi/v1/routeService/walking"
        const val mRouteBicyclingUrl =
            "https://mapapi.cloud.huawei.com/mapApi/v1/routeService/bicycling"
        const val mRouteDrivingUrl =
            "https://mapapi.cloud.huawei.com/mapApi/v1/routeService/driving"

        /* Tag Identifier */
        const val mMainActivityTag = "MainActivity"
        const val mMapHelperTag = "HmsGmsBPMapHelper"
        const val mMapHelperPresenterTag = "MapKitHelperPresenter"
        const val mNetClientTag = "NetClient"
        const val mNetworkRequestManagerTag = "NetworkRequestManager"
    }
}