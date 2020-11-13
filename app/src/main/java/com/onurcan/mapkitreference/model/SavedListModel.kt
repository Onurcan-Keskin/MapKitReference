package com.onurcan.mapkitreference.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.onurcan.mapkitreference.data.database.AppDatabase
import com.onurcan.mapkitreference.data.entity.SavedListDataClass
import com.onurcan.mapkitreference.data.repository.SavedListRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SavedListModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val savedList: LiveData<List<SavedListDataClass>>
    private val repo: SavedListRepo

    init {
        val savedListDao = AppDatabase.getDatabase(application,scope).savedListDao()
        repo = SavedListRepo(savedListDao!!)
        savedList = repo.savedList
    }

    fun insert(listDataClass: SavedListDataClass) = scope.launch(Dispatchers.IO){
        repo.insert(listDataClass)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun deleteAll(){
        repo.deleteAll()
    }

    fun deleteList(listDataClass: SavedListDataClass){
        repo.deleteList(listDataClass)
    }

    fun updateList(listDataClass: SavedListDataClass){
        repo.update(listDataClass)
    }
}