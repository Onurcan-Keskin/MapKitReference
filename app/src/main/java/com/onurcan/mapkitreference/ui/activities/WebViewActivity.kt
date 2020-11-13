package com.onurcan.mapkitreference.ui.activities

import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.databinding.ActivityWebViewBinding
import com.onurcan.mapkitreference.helper.LocaleHelper
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.onurcan.mapkitreference.helper.showSnackbar

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var context: Context

    private lateinit var sharedPrefs: SharedPrefsManager
    private var webUrl: String? = ""
    private var webName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        context = applicationContext!!
        sharedPrefs = SharedPrefsManager(this)
        /* Mode State */
        if (sharedPrefs.loadNightModeState())
            setTheme(R.style.DarkMode_withStatBar)
        else
            setTheme(R.style.LightMode_withStatBar)
        /* Language State */
        if (sharedPrefs.loadLanguage() == "tr")
            LocaleHelper.setLocale(this, "tr")
        else
            LocaleHelper.setLocale(this, "en")
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(this.layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val webSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        webUrl = intent.getStringExtra("url")
        webName = intent.getStringExtra("place")
        supportActionBar!!.title = webName

        if (sharedPrefs.loadNightModeState()) {
            binding.cnst.background = ContextCompat.getDrawable(this, R.drawable.colour_bg_dark)
            binding.webview.background = ContextCompat.getDrawable(this, R.drawable.colour_bg_dark)
        } else {
            binding.cnst.background = ContextCompat.getDrawable(this, R.drawable.colour_bg_light_1)
            binding.webview.background = ContextCompat.getDrawable(this, R.drawable.colour_bg_light_1)
        }

        binding.webview.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                showSnackbar(view!!, description!!)
            }
        }
        if (webUrl!!.contains("https") || webUrl!!.contains("http"))
            binding.webview.loadUrl(webUrl)
        else
            binding.webview.loadUrl("https://$webUrl")
    }
}