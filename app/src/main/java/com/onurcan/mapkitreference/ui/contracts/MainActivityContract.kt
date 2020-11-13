package com.onurcan.mapkitreference.ui.contracts

import android.app.Activity
import android.content.Context

class MainActivityContract {

    interface ViewMain{
        fun requestPermissions()
    }

    interface PresenterMain{
        fun onViewsCreate()
    }
}