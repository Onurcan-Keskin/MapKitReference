package com.onurcan.mapkitreference.ui.contracts

import android.app.Activity
import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.onurcan.mapkitreference.helper.SharedPrefsManager

class SettingsContract {
    interface SettingsPresenter{
        fun onSharedPrefs(sharedPrefsManager: SharedPrefsManager, dayNight: SwitchCompat, context: Context, activity: Activity)
        fun restartApp(context: Context,activity:Activity)
        fun tryToGetAppVersion(context: Context,versionText: TextView)
        fun getBackgroundAnim(constraintLayout: ConstraintLayout)
    }
}