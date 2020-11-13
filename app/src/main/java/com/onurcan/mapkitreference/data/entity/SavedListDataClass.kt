package com.onurcan.mapkitreference.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedList")
data class SavedListDataClass(
    @PrimaryKey(autoGenerate = true) val listId:Int,
    @ColumnInfo(name = "listName") val listName:String=""
)
