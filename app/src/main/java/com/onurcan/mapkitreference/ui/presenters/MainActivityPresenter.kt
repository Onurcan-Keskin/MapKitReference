package com.onurcan.mapkitreference.ui.presenters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.HorizontalScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.card.MaterialCardView
import com.onurcan.mapkitreference.helper.expandView
import com.onurcan.mapkitreference.ui.activities.MainActivity
import com.onurcan.mapkitreference.ui.contracts.MainActivityContract
import com.onurcan.mapkitreference.view.IMapHelper

class MainActivityPresenter constructor(
    private val IMapHelper: IMapHelper
) : MainActivityContract.PresenterMain {

    override fun onViewsCreate() {

    }

    fun restartApp(context: Context, activity: Activity) {
        context.startActivity(Intent(context.applicationContext, MainActivity::class.java))
        activity.recreate()
    }

}