package com.onurcan.mapkitreference.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.databinding.ActivitySettingsBinding
import com.onurcan.mapkitreference.helper.LocaleHelper
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.onurcan.mapkitreference.ui.presenters.SettingsPresenter
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val settingsPresenter : SettingsPresenter by lazy {
        SettingsPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefsManager = SharedPrefsManager(this)

        if (sharedPrefsManager.loadNightModeState()) {
            setTheme(R.style.DarkMode_withStatBar)
        } else {
            setTheme(R.style.LightMode_withStatBar)
        }

        if (sharedPrefsManager.loadLanguage() == "tr") {
            LocaleHelper.setLocale(this, "tr")
        } else {
            LocaleHelper.setLocale(this, "en")
        }

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        //supportActionBar?.hide()
        supportActionBar?.title = getString(R.string.settings)
        Slidr.attach(this)

        settingsPresenter.onSharedPrefs(
            sharedPrefsManager,
            day_night_mode,
            this,
            SettingsActivity()
        )

        if (sharedPrefsManager.loadNightModeState()) {
            binding.dayNightMode.text = getString(R.string.night)
            binding.settingsConstraint.background =
                ContextCompat.getDrawable(this, R.drawable.colour_bg_dark)
            binding.darkLottie.visibility = View.VISIBLE
            binding.lightLottie.visibility = View.GONE
        } else {
            binding.dayNightMode.text = getString(R.string.day)
            binding.settingsConstraint.background =
                ContextCompat.getDrawable(this, R.drawable.colour_bg_light_list)
            settingsPresenter.getBackgroundAnim(binding.root)
            binding.darkLottie.visibility = View.GONE
            binding.lightLottie.visibility = View.VISIBLE
        }

        if (sharedPrefsManager.loadLanguage() == "tr") {
            binding.trLottie.visibility = View.VISIBLE
            binding.enLottie.visibility = View.GONE
        } else {
            binding.trLottie.visibility = View.GONE
            binding.enLottie.visibility = View.VISIBLE
        }

        binding.trLng.setOnClickListener {
            sharedPrefsManager.setLanguage("tr")
            LocaleHelper.setLocale(this, "tr")
            recreate()
        }
        binding.enLng.setOnClickListener {
            sharedPrefsManager.setLanguage("en")
            LocaleHelper.setLocale(this, "en")
            recreate()
        }

        binding.appVersion.setOnClickListener {
            settingsPresenter.versionDialog(this,R.style.DialogSlide,sharedPrefsManager)
        }

        settingsPresenter.tryToGetAppVersion(this,binding.appVersion)
    }
}