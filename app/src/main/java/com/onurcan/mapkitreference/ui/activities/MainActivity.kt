package com.onurcan.mapkitreference.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.databinding.ActivityMainBinding
import com.onurcan.mapkitreference.helper.*
import com.onurcan.mapkitreference.model.MapKitModel
import com.onurcan.mapkitreference.ui.contracts.MainActivityContract
import com.onurcan.mapkitreference.ui.presenters.MainActivityPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.from_to_layout.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class MainActivity : AppCompatActivity(), MainActivityContract.PresenterMain,
    MainActivityContract.ViewMain {

    private val requestCodeAskPerms = 1

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context

    private lateinit var sharedPrefs: SharedPrefsManager
    private var constants: Int = 0

    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    //private lateinit var bottomSheetVideoBehavior: BottomSheetBehavior<*>

    private val mapKitModel: MapKitModel by lazy { MapKitModel(this) }

    private val mainPresenter: MainActivityPresenter by lazy {
        MainActivityPresenter(
            mapKitModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context = applicationContext!!
        sharedPrefs = SharedPrefsManager(this)
        /* Mode State */
        if (sharedPrefs.loadNightModeState())
            setTheme(R.style.DarkMode)
        else
            setTheme(R.style.LightMode)
        /* Language State */
        if (sharedPrefs.loadLanguage() == "tr")
            LocaleHelper.setLocale(this, "tr")
        else
            LocaleHelper.setLocale(this, "en")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mainPresenter.onViewsCreate()
        requestPermissions()
        mapKitModel.onSavedInstanceBundle(savedInstanceState)

        map_settings.setOnClickListener {
            startActivity(Intent(context.applicationContext, SettingsActivity::class.java))
        }

        KeyboardVisibilityEvent.setEventListener(this) { isOpen ->
            if (isOpen) {
                showLogInfo(Constants.mMainActivityTag, "Keyboard:  ON")
                // mapKitModel.onKeyboardSearchSite()
            } else {
                showLogInfo(Constants.mMainActivityTag, "Keyboard: OFF")
            }
        }

        poi_editText.setOnClickListener {
            constants = Constants.mRequestSiteLocation
            mapKitModel.onKeyboardSearchSite(Constants.mRequestSiteLocation)
        }

        from_to_your_location.setOnClickListener {
            constants = Constants.mRequestStartLocation
            mapKitModel.onKeyboardSearchStartSite(Constants.mRequestStartLocation)
        }

        from_to_destination_location.setOnClickListener {
            constants = Constants.mRequestDestinationLocation
            mapKitModel.onKeyboardSearchDestinationSite(Constants.mRequestDestinationLocation)
        }

    }

    override fun requestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                runtimePermissions, requestCodeAskPerms
            )
        } else {
            Log.d("Permissions Granted: ", "All")

        }
    }

    private fun String.permissionGranted(ctx: Context) =
        ContextCompat.checkSelfPermission(ctx, this) == PackageManager.PERMISSION_GRANTED

    private fun hasPermissions(): Boolean {
        return runtimePermissions.count { !it.permissionGranted(this) } == 0
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            requestCodeAskPerms -> {
                for (index in runtimePermissions.indices) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        var notGrantedMessage =
                            "Required permission ${permissions[index]} not granted."

                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[index]
                            )
                        ) {
                            notGrantedMessage += getString(R.string.error_no_perm)
                        }

                        Toast.makeText(this, notGrantedMessage, Toast.LENGTH_LONG).show()
                    }
                }
//                initMapFragmentView()
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showToast(this,constants.toString())
        mapKitModel.checkByControllerSite(
            requestCode,
            resultCode,
            data,
            constants
        )
    }

    override fun onViewsCreate() {

    }
}