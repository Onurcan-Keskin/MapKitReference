package com.onurcan.mapkitreference.ui.presenters

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.*
import android.location.Geocoder
import android.os.AsyncTask
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.TooltipCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.*
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.data.database.AppDatabase
import com.onurcan.mapkitreference.data.entity.SavedListDataClass
import com.onurcan.mapkitreference.helper.*
import com.onurcan.mapkitreference.model.SavedListModel
import com.onurcan.mapkitreference.ui.activities.MainActivity
import com.onurcan.mapkitreference.ui.adapters.MainHorizontalPoiAdapter
import com.onurcan.mapkitreference.ui.adapters.SavedListAdapter
import com.onurcan.mapkitreference.ui.contracts.MapKitMainContract
import com.roughike.bottombar.BottomBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

class MapKitMainPresenter : MapKitMainContract.ViewMainMap, MapKitMainContract.PresenterMainMap {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private lateinit var savedListModel: SavedListModel


    override fun uiVisible(
        mEditText: MaterialCardView,
        mLinearPoi: RecyclerView,
        mGo: MaterialCardView,
        mDetails: MaterialCardView
    ) {
        mEditText.visibility = View.VISIBLE
        mGo.visibility = View.VISIBLE
        mLinearPoi.visibility = View.VISIBLE
        mDetails.visibility = View.VISIBLE
    }

    override fun uiGone(
        mEditText: MaterialCardView,
        mLinearPoi: LinearLayout,
        mGo: MaterialCardView,
        mDetails: MaterialCardView,
        mDistance: ConstraintLayout
    ) {
        mEditText.visibility = View.GONE
        mGo.visibility = View.GONE
        mLinearPoi.visibility = View.GONE
        mDetails.visibility = View.GONE

    }

    override fun mapClicked(
        mEditText: MaterialCardView,
        mGo: MaterialCardView,
        mDetails: MaterialCardView,
        mAnim: BottomBar,
        mRecycler: RecyclerView,
        mDistance: ConstraintLayout
    ) {
        mEditText.expandView()
        mGo.expandView()
        mAnim.expandView()
        mDetails.expandView()
        mRecycler.expandView()
        mDistance.expandView()
    }

    override fun setPinTitle(lat: Double, long: Double, context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, long, 1)
        showLogDebug("Log", "$address")
        address.let {
            geocoder.getFromLocation(lat, long, 1)
        }.ifEmpty {
            geocoder.getFromLocation(lat, long, 1)
            return@ifEmpty address[0].getAddressLine(0)
        }
        //"$tst1, $tst2 - $tst3"
        return address[0].getAddressLine(0)
    }


    override fun onGoClicked(
        fromTo: CoordinatorLayout,
        materialEditCard: MaterialCardView,
        horizontalPoi: RecyclerView,
        mapDetails: MaterialCardView,
        goBy: ConstraintLayout,
        mapGo: MaterialCardView
    ) {
        fromTo.expandView()
        materialEditCard.expandView()
        horizontalPoi.expandView()
        mapDetails.expandView()
        mapGo.expandView()
        goBy.expandView()
    }

    override fun setMainPoiRecycler(
        context: Context,
        recyclerView: RecyclerView,
        lat: Double,
        long: Double,
        hMap: HuaweiMap?,
        sharedPrefsManager: SharedPrefsManager
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MainHorizontalPoiAdapter(context, lat, long, hMap, sharedPrefsManager)
        recyclerView.adapter = adapter
    }

    override fun copyAddressToClipboard(context: Context, addressTextView: TextView) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(
            context.getString(R.string.app_name),
            addressTextView.text.toString()
        )
        clipboardManager.setPrimaryClip(clip)
        val snack = Snackbar.make(
            addressTextView,
            R.string.clipboard_success,
            Snackbar.LENGTH_LONG
        )
        val view = snack.view
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        params.bottomMargin = 155
        view.layoutParams = params
        snack.show()
    }

    override fun showSheetFromPlacedMarker(
        context: Context,
        slideUpPanel: SlidingUpPanelLayout,
        lat: Double,
        long: Double,
        poiName: TextView,
        poiAddress: TextView,
        poiAddressLin: LinearLayout,
        poiHours: TextView,
        marker: Marker
    ) {
        slideUpPanel.visibility = View.VISIBLE
        val geocoder = Geocoder(context)
        val addressDetail = geocoder.getFromLocation(lat, long, 1)

        poiName.text = marker.title
        poiAddress.text = addressDetail[0].getAddressLine(0)
        poiHours.text = "$lat, $long"

        poiAddressLin.setOnLongClickListener {
            copyAddressToClipboard(context, poiAddress)
            return@setOnLongClickListener true
        }
    }

    override fun setLightPopUp(context: Context, mDetails: MaterialCardView, hMap: HuaweiMap) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val pw = inflater.inflate(R.layout.popup_map_light, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val pWind = PopupWindow(pw, width, height, focusable)

        val none = pw.findViewById<TextView>(R.id.map_s_none)
        val normal = pw.findViewById<TextView>(R.id.map_s_normal)
        val satellite = pw.findViewById<TextView>(R.id.map_s_satellite)
        val terrain = pw.findViewById<TextView>(R.id.map_s_terrain)
        val hybrid = pw.findViewById<TextView>(R.id.map_s_hybrid)

        val traffic = pw.findViewById<TextView>(R.id.map_d_traffic)
        val building = pw.findViewById<TextView>(R.id.map_d_building)
        val indoor = pw.findViewById<TextView>(R.id.map_d_indoor)

        val closePop = pw.findViewById<ImageButton>(R.id.close_popup)

        none.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_NONE
            pWind.dismiss()
        }

        normal.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_NORMAL
            pWind.dismiss()
        }

        satellite.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_SATELLITE
            pWind.dismiss()
        }

        terrain.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_TERRAIN
            pWind.dismiss()
        }

        hybrid.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_HYBRID
            pWind.dismiss()
        }

        traffic.setOnClickListener {
            if (hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = false
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: False")
                traffic.background = ContextCompat.getDrawable(context, R.drawable.ic_nothing)
                TooltipCompat.setTooltipText(
                    traffic,
                    context.getString(R.string.details_traffic_off)
                )
                showSnackbar(mDetails, context.getString(R.string.details_traffic_off))
                pWind.dismiss()
            } else if (!hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = true
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: True")
                traffic.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_foreground_selected)
                TooltipCompat.setTooltipText(
                    traffic,
                    context.getString(R.string.details_traffic_on)
                )
                showSnackbar(mDetails, context.getString(R.string.details_traffic_on))
                pWind.dismiss()
            }
        }

        building.setOnClickListener {
            if (hMap.isBuildingsEnabled) {
                hMap.isBuildingsEnabled = false
                showLogInfo(Constants.mMapHelperPresenterTag, "isBuildingsEnabled: False")
                building.background = ContextCompat.getDrawable(context, R.drawable.ic_nothing)
                TooltipCompat.setTooltipText(
                    building,
                    context.getString(R.string.details_building_off)
                )
                showSnackbar(mDetails, context.getString(R.string.details_building_off))
                pWind.dismiss()
            } else if (!hMap.isBuildingsEnabled) {
                hMap.isBuildingsEnabled = true
                showLogInfo(Constants.mMapHelperPresenterTag, "isBuildingsEnabled: True")
                building.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_foreground_selected)
                TooltipCompat.setTooltipText(
                    building,
                    context.getString(R.string.details_building_on)
                )
                showSnackbar(mDetails, context.getString(R.string.details_building_on))
                pWind.dismiss()
            }
        }

        indoor.setOnClickListener {
            if (hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = false
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: False")
                indoor.background = ContextCompat.getDrawable(context, R.drawable.ic_nothing)
                TooltipCompat.setTooltipText(indoor, context.getString(R.string.details_indoor_off))
                showSnackbar(mDetails, context.getString(R.string.details_indoor_off))
                pWind.dismiss()
            } else if (!hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = true
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: True")
                indoor.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_foreground_selected)
                TooltipCompat.setTooltipText(indoor, context.getString(R.string.details_indoor_on))
                showSnackbar(mDetails, context.getString(R.string.details_indoor_on))
                pWind.dismiss()
            }
        }

        closePop.setOnClickListener { pWind.dismiss() }

        pWind.showAtLocation(mDetails, Gravity.BOTTOM, -120, 150)
    }

    override fun setDarkPopUp(context: Context, mDetails: MaterialCardView, hMap: HuaweiMap) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val pw = inflater.inflate(R.layout.popup_map_dark, null)
        pw.animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val pWind = PopupWindow(pw, width, height, focusable)

        val none = pw.findViewById<TextView>(R.id.map_s_none)
        val normal = pw.findViewById<TextView>(R.id.map_s_normal)
        val satellite = pw.findViewById<TextView>(R.id.map_s_satellite)
        val terrain = pw.findViewById<TextView>(R.id.map_s_terrain)
        val hybrid = pw.findViewById<TextView>(R.id.map_s_hybrid)

        val traffic = pw.findViewById<TextView>(R.id.map_d_traffic)
        val building = pw.findViewById<TextView>(R.id.map_d_building)
        val indoor = pw.findViewById<TextView>(R.id.map_d_indoor)

        val closePop = pw.findViewById<ImageButton>(R.id.close_popup)

        none.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_NONE
            pWind.dismiss()
        }

        normal.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_NORMAL
            pWind.dismiss()
        }

        satellite.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_SATELLITE
            pWind.dismiss()
        }

        terrain.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_TERRAIN
            pWind.dismiss()
        }

        hybrid.setOnClickListener {
            hMap.mapType = HuaweiMap.MAP_TYPE_HYBRID
            pWind.dismiss()
        }

        traffic.setOnClickListener {
            if (hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = false
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: False")
                traffic.background = ContextCompat.getDrawable(context, R.drawable.ic_nothing)
                TooltipCompat.setTooltipText(
                    traffic,
                    context.getString(R.string.details_traffic_off)
                )
                showSnackbar(mDetails, context.getString(R.string.details_traffic_off))
                pWind.dismiss()
            } else if (!hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = true
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: True")
                traffic.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_foreground_selected)
                TooltipCompat.setTooltipText(
                    traffic,
                    context.getString(R.string.details_traffic_on)
                )
                showSnackbar(mDetails, context.getString(R.string.details_traffic_on))
                pWind.dismiss()
            }
        }

        building.setOnClickListener {
            if (hMap.isBuildingsEnabled) {
                hMap.isBuildingsEnabled = false
                showLogInfo(Constants.mMapHelperPresenterTag, "isBuildingsEnabled: False")
                building.background = ContextCompat.getDrawable(context, R.drawable.ic_nothing)
                TooltipCompat.setTooltipText(
                    building,
                    context.getString(R.string.details_building_off)
                )
                showSnackbar(mDetails, context.getString(R.string.details_building_off))
                pWind.dismiss()
            } else if (!hMap.isBuildingsEnabled) {
                hMap.isBuildingsEnabled = true
                showLogInfo(Constants.mMapHelperPresenterTag, "isBuildingsEnabled: True")
                building.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_foreground_selected)
                TooltipCompat.setTooltipText(
                    building,
                    context.getString(R.string.details_building_on)
                )
                showSnackbar(mDetails, context.getString(R.string.details_building_on))
                pWind.dismiss()
            }
        }

        indoor.setOnClickListener {
            if (hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = false
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: False")
                indoor.background = ContextCompat.getDrawable(context, R.drawable.ic_nothing)
                TooltipCompat.setTooltipText(indoor, context.getString(R.string.details_indoor_off))
                showSnackbar(mDetails, context.getString(R.string.details_indoor_off))
                pWind.dismiss()
            } else if (!hMap.isTrafficEnabled) {
                hMap.isTrafficEnabled = true
                showLogInfo(Constants.mMapHelperPresenterTag, "isTrafficEnabled: True")
                indoor.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_foreground_selected)
                TooltipCompat.setTooltipText(indoor, context.getString(R.string.details_indoor_on))
                showSnackbar(mDetails, context.getString(R.string.details_indoor_on))
                pWind.dismiss()
            }
        }

        closePop.setOnClickListener { pWind.dismiss() }

        pWind.showAtLocation(mDetails, Gravity.BOTTOM, -120, 150)
    }

    override fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String) {
        if (null == hMap) return
        hMap.setMarkersClustering(true)
        hMap.addMarker(
            MarkerOptions().position(lng).title(pinName).clusterable(true).draggable(false)
        )
    }

    fun openList(
        context: Context,
        activity: MainActivity,
        type: Int,
        sharedPrefsManager: SharedPrefsManager,
        marker: Marker
    ) {
        val dialog = Dialog(context, R.style.BlurTheme)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setLayout(width, height)
        dialog.window!!.attributes.windowAnimations = type
        dialog.setContentView(R.layout.dialog_create_list)
        dialog.setCanceledOnTouchOutside(false)

        val done = dialog.findViewById<TextView>(R.id.d_done)
        val appbar = dialog.findViewById<AppBarLayout>(R.id.d_appBarLayout)
        val back = dialog.findViewById<ImageButton>(R.id.d_back)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.d_recycler)
        val addNewList = dialog.findViewById<MaterialCardView>(R.id.d_new_list)

        if (sharedPrefsManager.loadNightModeState()) {
            appbar.background = ContextCompat.getDrawable(context, R.drawable.action_bar_bg_dark)
        } else {
            appbar.background = ContextCompat.getDrawable(context, R.drawable.action_bar_bg_light)
        }

        back.setOnClickListener {
            dialog.dismiss()
        }

        done.setOnClickListener {

        }

        addNewList.setOnClickListener { createNewList(context, type, sharedPrefsManager) }
        setSavedList(context, activity, recyclerView, marker, type, sharedPrefsManager)
        dialog.show()
    }

    private fun setSavedList(
        context: Context,
        activity: MainActivity,
        recyclerView: RecyclerView,
        marker: Marker,
        type: Int,
        sharedPrefsManager: SharedPrefsManager
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SavedListAdapter(context, activity, marker, sharedPrefsManager)
        recyclerView.adapter = adapter

        savedListModel = ViewModelProvider(
            activity,
            ViewModelProvider.AndroidViewModelFactory(activity.application)
        ).get(
            SavedListModel::class.java
        )
        savedListModel = ViewModelProvider(activity).get(SavedListModel::class.java)

        savedListModel.savedList.observe(activity, { list ->
            list?.let { adapter.setList(it) }
        })

        swipeRecyclerActions(
            recyclerView,
            adapter,
            savedListModel,
            context
        )
    }

    private fun createNewList(
        context: Context,
        type: Int,
        sharedPrefsManager: SharedPrefsManager
    ) {
        val dialog = Dialog(context, R.style.BlurTheme)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setElevation(3F)
        dialog.window!!.setLayout(width, height)
        dialog.window!!.attributes.windowAnimations = type
        dialog.setContentView(R.layout.dialog_pn)
        dialog.setCanceledOnTouchOutside(false)

        val positiveBtn = dialog.findViewById<TextView>(R.id.d_save)
        val negativeBtn = dialog.findViewById<TextView>(R.id.d_cancel)
        val lin = dialog.findViewById<LinearLayout>(R.id.d_lin)
        val listName = dialog.findViewById<TextInputEditText>(R.id.d_list_name)

        if (sharedPrefsManager.loadNightModeState()) {
            lin.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_dark)
        } else {
            lin.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_light_1)
        }

        positiveBtn.setOnClickListener {
            AsyncTask.execute {
                val saveList = SavedListDataClass(
                    System.currentTimeMillis().toInt(),
                    listName.text.toString()
                )
                AppDatabase.getDatabase(context, scope).savedListDao()!!.insertAll(saveList)
                dialog.dismiss()
            }
        }

        negativeBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun swipeRecyclerActions(
        recyclerView: RecyclerView,
        adapter: SavedListAdapter,
        savedListModel: SavedListModel,
        context: Context
    ) {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val mList = adapter.getListPosition(position)
                if (direction == ItemTouchHelper.LEFT) {
                    savedListModel.deleteList(mList)
                    Snackbar.make(
                        recyclerView,
                        mList.listName + " " + context.getString(R.string.deleted),
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Undo") { _ ->
                            savedListModel.insert(mList)
                        }
                        .show()
                } else {
                    //createNewList(context, type, sharedPrefsManager)
                    val builder = AlertDialog.Builder(context)
                    val userEdit = EditText(context)
                    userEdit.hint = context.getString(R.string.list_name)
                    userEdit.gravity = Gravity.CENTER
                    userEdit.ellipsize
                    builder.setTitle(context.getString(R.string.update_list_name))
                        .setView(userEdit)
                        .setPositiveButton(context.getString(R.string.save)) { _, _ ->
                            val listName = userEdit.text.toString()
                            if (listName.isEmpty()) {
                                showToast(context, context.getString(R.string.error_List_name))
                            } else {
                                val name = SavedListDataClass(position, listName)
                                savedListModel.updateList(name)
                                savedListModel.savedList
                            }
                        }
                        .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    builder.show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    val p = Paint()

                    if (dX > 0) {
                        /* Edit */
                        p.color = Color.parseColor("#00A344")
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)

                        val left = itemView.left.toFloat() + width
                        val top = itemView.top.toFloat() + width
                        val right = itemView.left.toFloat() + 2 * width
                        val bottom = itemView.bottom.toFloat() - width

                        icon = getBitmapFromVectorDrawable(context, R.drawable.ic_edit_w)
                        val iconDest = RectF(left, top, right, bottom)

                        c.drawBitmap(icon, null, iconDest, p)
                    } else {
                        /* Clear */
                        p.color = Color.parseColor("#CB1A1A")

                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)


                        icon = getBitmapFromVectorDrawable(context, R.drawable.ic_close)

                        val left = itemView.right.toFloat() - 2 * width
                        val top = itemView.top.toFloat() + width
                        val right = itemView.right.toFloat() - width
                        val bottom = itemView.bottom.toFloat() - width
                        val iconDest = RectF(left, top, right, bottom)

                        c.drawBitmap(icon, null, iconDest, p)
                    }
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })

        helper.attachToRecyclerView(recyclerView)
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableInt: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableInt)

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    override fun moveCamera(hMap: HuaweiMap?, latLng: LatLng) {
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(20F)
            //.bearing(31.5F)
            .tilt(2.2F)
            .build()
        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap!!.animateCamera(cu)
    }

    override fun setPrefs(
        sharedPrefsManager: SharedPrefsManager,
        context: Context,
        slidePin: RelativeLayout,
        slideAround: LinearLayout,
        animatedBottomBar: BottomBar,
        hMap: HuaweiMap?
    ) {
        if (sharedPrefsManager.loadNightModeState()) {
            context.setTheme(R.style.DarkMode)
            val styleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_night)
            hMap!!.setMapStyle(styleOptions)
            slidePin.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_dark)
            slideAround.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_dark)
            animatedBottomBar.background =
                ContextCompat.getDrawable(context, R.drawable.colour_bottom_bar_dark)
        } else {
            context.setTheme(R.style.LightMode)
            val styleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_day)
            hMap!!.setMapStyle(styleOptions)
            slidePin.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_light)
            slideAround.background =
                ContextCompat.getDrawable(context, R.drawable.card_top_corners_light)
            animatedBottomBar.background =
                ContextCompat.getDrawable(context, R.drawable.colour_bottom_bar_light)
        }
    }
}
