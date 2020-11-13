package com.onurcan.mapkitreference.ui.presenters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.onurcan.mapkitreference.ui.activities.SettingsActivity
import com.onurcan.mapkitreference.ui.activities.WebViewActivity
import com.onurcan.mapkitreference.ui.contracts.SettingsContract

class SettingsPresenter : SettingsContract.SettingsPresenter {
    override fun onSharedPrefs(
        sharedPrefsManager: SharedPrefsManager,
        dayNight: SwitchCompat,
        context: Context,
        activity: Activity
    ) {
        if (sharedPrefsManager.loadNightModeState()) {
            dayNight.isChecked = true
            dayNight.text = context.getText(R.string.day)
        } else {
        }
        dayNight.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sharedPrefsManager.setNightModeState(true)
                dayNight.text = context.getText(R.string.night)
                restartApp(context, activity)
            } else {
                sharedPrefsManager.setNightModeState(false)
                restartApp(context, activity)
            }
        }
    }

    override fun restartApp(context: Context, activity: Activity) {
        context.startActivity(Intent(context.applicationContext, SettingsActivity::class.java))
        activity.recreate()
    }

    override fun tryToGetAppVersion(context: Context, versionText: TextView) {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = info.versionName
            versionText.text = context.getString(R.string.version) + " $version"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun getBackgroundAnim(constraintLayout: ConstraintLayout) {
        val animDrawable = constraintLayout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(2000)
        animDrawable.setExitFadeDuration(4000)
        animDrawable.start()
    }

    fun versionDialog(context: Activity, type: Int, sharedPrefsManager: SharedPrefsManager) {
        val dialog = Dialog(context, R.style.BlurTheme)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setLayout(width, height)
        dialog.window!!.attributes.windowAnimations = type
        dialog.setContentView(R.layout.dialog_library)
        dialog.setCanceledOnTouchOutside(false)

        val linLayout = dialog.findViewById<LinearLayout>(R.id.d_library_ln_layout)
        val appbar = dialog.findViewById<AppBarLayout>(R.id.d_appBarLayout)
        val back = dialog.findViewById<ImageButton>(R.id.d_back)

        val mapName = dialog.findViewById<TextView>(R.id.map_name)
        val mapUrl = dialog.findViewById<TextView>(R.id.map_url)
        mapUrl.setOnClickListener { urlWebView(context,mapUrl,mapName) }

        val siteName = dialog.findViewById<TextView>(R.id.site_name)
        val siteUrl = dialog.findViewById<TextView>(R.id.site_url)
        siteUrl.setOnClickListener { urlWebView(context,siteUrl,siteName) }

        val locName = dialog.findViewById<TextView>(R.id.loc_name)
        val locUrl = dialog.findViewById<TextView>(R.id.loc_url)
        locUrl.setOnClickListener { urlWebView(context,locUrl,locName) }

        val picassoName = dialog.findViewById<TextView>(R.id.picass_name)
        val picassoUrl = dialog.findViewById<TextView>(R.id.picass_url)
        picassoUrl.setOnClickListener { urlWebView(context,picassoName,picassoUrl) }

        val okhttpName = dialog.findViewById<TextView>(R.id.okhttp_name)
        val okhttpUrl = dialog.findViewById<TextView>(R.id.okhttp_url)
        okhttpUrl.setOnClickListener { urlWebView(context,okhttpUrl,okhttpUrl) }

        val okioName = dialog.findViewById<TextView>(R.id.okio_name)
        val okioUrl = dialog.findViewById<TextView>(R.id.okio_url)
        okioUrl.setOnClickListener { urlWebView(context,okioUrl,okioName) }

        val lottieName = dialog.findViewById<TextView>(R.id.lottie_name)
        val lottieUrl = dialog.findViewById<TextView>(R.id.lottie_url)
        lottieUrl.setOnClickListener { urlWebView(context,lottieUrl,lottieName) }

        val slideName = dialog.findViewById<TextView>(R.id.slide_name)
        val slideUrl = dialog.findViewById<TextView>(R.id.slide_url)
        slideUrl.setOnClickListener { urlWebView(context,slideUrl,slideName) }

        val netName = dialog.findViewById<TextView>(R.id.net_name)
        val netUrl = dialog.findViewById<TextView>(R.id.net_url)
        netUrl.setOnClickListener { urlWebView(context, netUrl,netName) }

        val circleName = dialog.findViewById<TextView>(R.id.circle_name)
        val circleUrl = dialog.findViewById<TextView>(R.id.circle_url)
        circleUrl.setOnClickListener { urlWebView(context,circleUrl,circleName) }

        val keyName = dialog.findViewById<TextView>(R.id.key_name)
        val keyUrl = dialog.findViewById<TextView>(R.id.key_url)
        keyUrl.setOnClickListener { urlWebView(context,keyUrl,keyName) }

        val bottomName = dialog.findViewById<TextView>(R.id.bottom_name)
        val bottomUrl = dialog.findViewById<TextView>(R.id.bottom_url)
        bottomUrl.setOnClickListener { urlWebView(context,bottomUrl,bottomName) }

        val animName = dialog.findViewById<TextView>(R.id.anim_name)
        val animUrl = dialog.findViewById<TextView>(R.id.anim_url)
        animUrl.setOnClickListener { urlWebView(context,animUrl,animName) }

        if (sharedPrefsManager.loadNightModeState()) {
            linLayout.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_dark)
            appbar.background = ContextCompat.getDrawable(context, R.drawable.action_bar_bg_dark)
        } else {
            linLayout.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_light_1)
            appbar.background = ContextCompat.getDrawable(context, R.drawable.action_bar_bg_light)
        }

        back.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun urlWebView(context: Context, textViewUrl: TextView,textView: TextView){
        val string = textViewUrl.text.toString().split(" ")
        val mString = string[1]
        context.startActivity(Intent(context.applicationContext,WebViewActivity::class.java)
            .putExtra("url",mString)
            .putExtra("place",textView.text.toString()))
    }
}