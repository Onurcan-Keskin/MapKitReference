package com.onurcan.mapkitreference.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.onurcan.mapkitreference.data.database.AppDatabase
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass
import com.onurcan.mapkitreference.data.repository.PoiItemRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PoiItemModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val poiItem: LiveData<List<PoiItemDataClass>>
    private val repo: PoiItemRepo

    init {
        val poiItemDao = AppDatabase.getDatabase(application, scope).poiItemDao()
        repo = PoiItemRepo(poiItemDao!!)
        poiItem = repo.poiItem
    }

    fun insert(list: PoiItemDataClass) = scope.launch(Dispatchers.IO) {
        repo.insert(list)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun deleteAll(){
        repo.deleteAll()
    }

    fun deleteItem(list: PoiItemDataClass){
        repo.deleteList(list)
    }
}