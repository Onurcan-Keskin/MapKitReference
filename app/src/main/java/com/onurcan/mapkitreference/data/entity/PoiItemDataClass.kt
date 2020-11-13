package com.onurcan.mapkitreference.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedPoiItem") // Case Sensitive
data class PoiItemDataClass(
    @PrimaryKey(autoGenerate = true) val poiId: Int,
    @ColumnInfo(name = "poiName") val poiName: String = "",
    @ColumnInfo(name = "listName") val listName: String = "",
    @ColumnInfo(name = "poiLat") val poiLat: String = "",
    @ColumnInfo(name = "poiLong") val poiLong: String = "",
    @ColumnInfo(name = "poiAddress") val poiAddress: String = "",
    @ColumnInfo(name = "poiWebAddress") val poiWebAddress: String = "",
    @ColumnInfo(name = "poiTimestamp") val poiTimestamp: String = ""
)
