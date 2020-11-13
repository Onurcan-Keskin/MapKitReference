package com.onurcan.mapkitreference.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.onurcan.mapkitreference.data.dao.PoiItemDao
import com.onurcan.mapkitreference.data.dao.SavedListDao
import com.onurcan.mapkitreference.data.entity.PoiItemDataClass
import com.onurcan.mapkitreference.data.entity.SavedListDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [SavedListDataClass::class, PoiItemDataClass::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun poiItemDao(): PoiItemDao?
    abstract fun savedListDao(): SavedListDao?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val NUMBER_OF_THREADS = 1
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance

            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }

        private class AppDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                INSTANCE?.let {
                    scope.launch(Dispatchers.IO) {
                    }
                }
            }
        }


    }
}