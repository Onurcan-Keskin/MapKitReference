package com.onurcan.mapkitreference.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.onurcan.mapkitreference.data.entity.SavedListDataClass

@Dao
interface SavedListDao {
    @Query(value = "SELECT * FROM savedlist")
    fun getAll(): LiveData<List<SavedListDataClass>>

    @Insert
    fun insertAll(vararg savedList: SavedListDataClass?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(savedList:SavedListDataClass)

    @Query("DELETE FROM savedlist")
    fun deleteAll()

    @Update
    fun update(savedList: SavedListDataClass)

    @Query("UPDATE SavedList SET listName = :name WHERE listId ==:id")
    fun updateItem(name:String, id:Int)

    @Delete
    fun deleteList(savedList: SavedListDataClass)
}