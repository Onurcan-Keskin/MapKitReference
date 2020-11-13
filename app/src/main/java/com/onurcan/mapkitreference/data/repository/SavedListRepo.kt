package com.onurcan.mapkitreference.data.repository

import android.os.AsyncTask
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.onurcan.mapkitreference.data.dao.SavedListDao
import com.onurcan.mapkitreference.data.entity.SavedListDataClass

class SavedListRepo(private val savedListDao: SavedListDao) {

    val savedList: LiveData<List<SavedListDataClass>> = savedListDao.getAll()

    @WorkerThread
    fun insert(savedList: SavedListDataClass){
        savedListDao.insertAll(savedList)
    }

    /*--------------- Delete All ---------------*/

    fun deleteAll(){
        DeleteAllListAsyncTask(savedListDao).execute()
    }

    private class DeleteAllListAsyncTask(private val mAsyncTask: SavedListDao):
            AsyncTask<Void,Void,Void>(){
        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTask.deleteAll()
            return null
        }
    }

    /*--------------- Delete Single ---------------*/

    fun deleteList(savedList: SavedListDataClass){
        DeleteListAsyncTask(savedListDao).execute(savedList)
    }

    private class DeleteListAsyncTask(private val mAsyncTask: SavedListDao):
    AsyncTask<SavedListDataClass,Void,Void>(){
        override fun doInBackground(vararg params: SavedListDataClass): Void? {
            mAsyncTask.deleteList(params[0])
            return null
        }
    }

    /*--------------- Update ---------------*/

    fun update(savedList: SavedListDataClass){
        UpdateListAsyncTask(savedListDao).execute(savedList)
    }

    private class UpdateListAsyncTask(private val mAsyncTask: SavedListDao):
            AsyncTask<SavedListDataClass,Void,Void>(){
        override fun doInBackground(vararg params: SavedListDataClass): Void? {
            mAsyncTask.update(params[0])
            return  null
        }
    }
}