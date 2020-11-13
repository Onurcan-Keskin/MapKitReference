package com.onurcan.mapkitreference.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass

@Dao
interface PoiItemDao {
    @Query("SELECT * FROM savedpoiitem")
    fun getAll(): LiveData<List<PoiItemDataClass>>

    @Insert
    fun insertAll(vararg savedPoi: PoiItemDataClass?)

    @Delete
    fun delete(savedPoi: PoiItemDataClass?)

    @Query("DELETE FROM SavedPoiItem")
    fun deleteAll()
}