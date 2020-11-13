package com.onurcan.mapkitreference.data.repository

import android.os.AsyncTask
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.onurcan.mapkitreference.data.dao.PoiItemDao
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass

class PoiItemRepo(private val poiItemDao: PoiItemDao) {
    val poiItem : LiveData<List<PoiItemDataClass>> = poiItemDao.getAll()

    @WorkerThread
    fun insert(poiItemDataClass: PoiItemDataClass){
        poiItemDao.insertAll(poiItemDataClass)
    }

    /*--------------- Delete All ---------------*/

    fun deleteAll(){
        DeleteAllItemAsyncTask(poiItemDao).execute()
    }

    private class DeleteAllItemAsyncTask(private val mAsyncTask: PoiItemDao):
    AsyncTask<Void,Void,Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTask.deleteAll()
            return null
        }
    }

    /*--------------- Delete Single ---------------*/

    fun deleteList(poiItem:PoiItemDataClass){
        DeleteSingleItemAsyncTask(poiItemDao).execute(poiItem)
    }

    private class DeleteSingleItemAsyncTask(private val mAsyncTask: PoiItemDao):
            AsyncTask<PoiItemDataClass,Void,Void>() {
        override fun doInBackground(vararg params: PoiItemDataClass): Void? {
            mAsyncTask.delete(params[0])
            return null
        }
    }
}