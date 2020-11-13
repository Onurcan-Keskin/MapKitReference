package com.onurcan.mapkitreference.ui.adapters

import android.app.Dialog
import android.content.Context
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.maps.model.Marker
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.data.database.AppDatabase
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass
import com.onurcan.mapkitreference.data.entity.SavedListDataClass
import com.onurcan.mapkitreference.helper.SharedPrefsManager
import com.onurcan.mapkitreference.helper.showLogDebug
import com.onurcan.mapkitreference.helper.showSnackbar
import com.onurcan.mapkitreference.model.PoiItemModel
import com.onurcan.mapkitreference.ui.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

class SavedListAdapter internal constructor(
    context: Context,
    activity: MainActivity,
    marker: Marker,
    sharedPrefsManager: SharedPrefsManager,
) :
    RecyclerView.Adapter<SavedListAdapter.ViewHolder>() {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private lateinit var poiItemModel: PoiItemModel

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var savedList = emptyList<SavedListDataClass>()
    private val mContext = context
    private val mActivity = activity
    private val mMarker = marker
    private val mSharedPrefsManager = sharedPrefsManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.single_list_name_roomdb, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedList.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = savedList[position]
        if (currentItem.listName.decapitalize(Locale.ROOT).trim() == "favorites" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favorite" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favori" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favoriler"
        ) {
            holder.listImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_heart
                )
            )
        } else if (currentItem.listName.decapitalize(Locale.ROOT).trim() == "home" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "ev"
        ) {
            holder.listImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_home))
        } else {
            holder.listImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_list))
        }
        holder.textListName.text = currentItem.listName
        holder.parent.setOnClickListener {
            savedPoiToList(
                mContext,
                R.style.DialogSlide,
                currentItem,
                holder,
                mMarker,
                mSharedPrefsManager
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun savedPoiToList(
        context: Context,
        type: Int,
        currentItem: SavedListDataClass,
        holder: ViewHolder,
        marker: Marker,
        sharedPrefsManager: SharedPrefsManager
    ) {
        val dialog = Dialog(context, R.style.BlurTheme)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setElevation(3F)
        dialog.window!!.setLayout(width, height)
        dialog.window!!.attributes.windowAnimations = type
        dialog.setContentView(R.layout.dialog_t_pn)
        dialog.setCanceledOnTouchOutside(false)

        val positiveBtn = dialog.findViewById<TextView>(R.id.d_save)
        val negativeBtn = dialog.findViewById<TextView>(R.id.d_cancel)

        val lin = dialog.findViewById<LinearLayout>(R.id.d_lin)
        val listName = dialog.findViewById<TextView>(R.id.d_text)

        if (sharedPrefsManager.loadNightModeState()) {
            lin.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_dark)
        } else {
            lin.background = ContextCompat.getDrawable(context, R.drawable.colour_bg_light_1)
        }

        val text =
            context.getString(R.string.prompt_save, marker.title, currentItem.listName)
        val styledText = Html.fromHtml(text, FROM_HTML_MODE_LEGACY)
        listName.text = styledText

        positiveBtn.setOnClickListener {
            AsyncTask.execute {
                val poiItem = PoiItemDataClass(
                    System.currentTimeMillis().toInt(),
                    mMarker.title,
                    currentItem.listName,
                    mMarker.position.latitude.toString(),
                    mMarker.position.longitude.toString(),
                    setPinTitle(mMarker, mContext),
                    "",
                    System.currentTimeMillis().toString()
                )
                AppDatabase.getDatabase(mContext, scope).poiItemDao()!!.insertAll(poiItem)
                dialog.dismiss()
                showSnackbar(holder.parent, context.getString(R.string.prompt_saved))
            }
        }
        negativeBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    internal fun setList(list: List<SavedListDataClass>) {
        this.savedList = list
        notifyDataSetChanged()
    }

    fun getListPosition(position: Int): SavedListDataClass {
        return savedList[position]
    }

    private fun setPinTitle(marker: Marker, context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address =
            geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
        showLogDebug("Log", "$address")
        address.let {
            geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
        }.ifEmpty {
            geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
            return@ifEmpty address[0].getAddressLine(0)
        }
        //"$tst1, $tst2 - $tst3"
        return address[0].getAddressLine(0)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView

        val textListName: TextView
        val listImg: ImageView

        init {
            textListName = parent.findViewById(R.id.list_name)
            listImg = parent.findViewById(R.id.list_image)
        }
    }
}