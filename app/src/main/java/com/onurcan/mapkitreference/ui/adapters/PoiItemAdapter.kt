package com.onurcan.mapkitreference.ui.adapters

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.onurcan.mapkitreference.R
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass
import com.onurcan.mapkitreference.model.PoiItemModel
import com.onurcan.mapkitreference.ui.activities.MainActivity
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.util.*

class PoiItemAdapter internal constructor(
    context: Context,
    activity: MainActivity, huaweiMap: HuaweiMap,
    slidingUpPanelLayout: SlidingUpPanelLayout
) :
    RecyclerView.Adapter<PoiItemAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var poiItem = emptyList<PoiItemDataClass>()
    private val mContext = context
    private val mHuaweiMap = huaweiMap
    private val mActivity = activity
    private val mSlidingUpPanelLayout = slidingUpPanelLayout

    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet

    private lateinit var poiItemModel: PoiItemModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.single_poi_card_roomdb, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = poiItem[position]
        if (currentItem.listName.decapitalize(Locale.ROOT).trim() == "favorites" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favorite" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favori" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "favoriler"
        ) {
            holder.poiListImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_heart
                )
            )
        } else if (currentItem.listName.decapitalize(Locale.ROOT).trim() == "home" ||
            currentItem.listName.decapitalize(Locale.ROOT).trim() == "ev"
        ) {
            holder.poiListImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_home
                )
            )
        } else {
            holder.poiListImg.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_list
                )
            )
        }
        holder.poiName.text = currentItem.poiName
        holder.poiInfo.text = currentItem.poiAddress
        holder.poiListName.text = currentItem.listName
        val latLng = LatLng(currentItem.poiLat.toDouble(), currentItem.poiLong.toDouble())
        val pName = currentItem.poiAddress
        holder.poiLinLayout.setOnClickListener {
            mSlidingUpPanelLayout.visibility = View.GONE
            addMarker(mHuaweiMap, latLng, pName)
            moveCamera(mHuaweiMap, latLng)
        }

        val scale = mContext.applicationContext.resources.displayMetrics.density
        holder.poiLinLayout.cameraDistance = 8000 * scale
        holder.poiLinBack.cameraDistance = 8000 * scale

        frontAnim =
            AnimatorInflater.loadAnimator(mContext, R.animator.front_animator) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(mContext, R.animator.back_animator) as AnimatorSet

        poiItemModel = ViewModelProvider(
            mActivity,
            ViewModelProvider.AndroidViewModelFactory(mActivity.application)
        ).get(
            PoiItemModel::class.java
        )

        val mList = getItemPosition(position)

        holder.poiLinLayout.setOnLongClickListener {
            frontAnim.setTarget(holder.poiLinLayout)
            backAnim.setTarget(holder.poiLinBack)
            frontAnim.start()
            backAnim.start()
            holder.poiLinLayout.visibility = View.GONE
            holder.poiLinBack.visibility = View.VISIBLE
            holder.clear.setOnClickListener {
                frontAnim.setTarget(holder.poiLinBack)
                backAnim.setTarget(holder.poiLinLayout)
                frontAnim.start()
                backAnim.start()
                holder.poiLinLayout.visibility = View.VISIBLE
                holder.poiLinBack.visibility = View.GONE
            }
            holder.deletePin.setOnClickListener {
                frontAnim.setTarget(holder.poiLinBack)
                backAnim.setTarget(holder.poiLinLayout)
                frontAnim.start()
                backAnim.start()
                holder.poiLinLayout.visibility = View.VISIBLE
                holder.poiLinBack.visibility = View.GONE
                poiItemModel.deleteItem(getItemPosition(holder.adapterPosition))
                val snack = Snackbar.make(
                    mSlidingUpPanelLayout,
                    mList.poiAddress + " " + mContext.getString(R.string.deleted),
                    Snackbar.LENGTH_LONG
                )
                val view = snack.view
                val params = view.layoutParams as CoordinatorLayout.LayoutParams
                params.gravity = Gravity.BOTTOM
                params.bottomMargin = 125
                view.layoutParams = params
                snack.setAction("Undo") { _ ->
                    poiItemModel.insert(mList)
                }
                snack.show()
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return poiItem.size
    }

    internal fun setItem(list: List<PoiItemDataClass>) {
        this.poiItem = list
        notifyDataSetChanged()
    }

    fun getItemPosition(position: Int): PoiItemDataClass {
        return poiItem[position]
    }

    private fun moveCamera(hMap: HuaweiMap?, latLng: LatLng) {
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(20F)
            //.bearing(31.5F)
            .tilt(2.2F)
            .build()
        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap!!.animateCamera(cu)
    }

    private fun addMarker(hMap: HuaweiMap?, lng: LatLng, pinName: String) {
        if (null == hMap) return
        hMap.setMarkersClustering(true)
        hMap.addMarker(
            MarkerOptions().position(lng).title(pinName).clusterable(true).draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_star_pin))
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent = itemView

        val poiName: TextView
        val poiInfo: TextView
        val poiListImg: ImageView
        val poiListName: TextView
        val poiLinLayout: LinearLayout

        val poiLinBack: LinearLayout
        val deletePin: TextView
        val clear: TextView
        val parentCard: MaterialCardView

        init {
            poiName = parent.findViewById(R.id.single_poi_address)
            poiInfo = parent.findViewById(R.id.single_poi_info)
            poiListImg = parent.findViewById(R.id.single_poi_list_img)
            poiListName = parent.findViewById(R.id.single_poi_list_name)
            poiLinLayout = parent.findViewById(R.id.single_poi_ly)

            poiLinBack = parent.findViewById(R.id.single_poi_long_press_lyt)
            deletePin = parent.findViewById(R.id.delete_pin)
            clear = parent.findViewById(R.id.clear)
            parentCard = parent.findViewById(R.id.parentCard)
        }
    }
}