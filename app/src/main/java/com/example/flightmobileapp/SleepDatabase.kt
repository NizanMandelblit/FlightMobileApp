package com.example.flightmobileapp

import android.content.Context
import androidx.room.*

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: SleepDatabase? = null
        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = newDatabase(context)
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}

fun newDatabase(context: Context): SleepDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        SleepDatabase::class.java,
        "sleep_history_database"
    )
        .fallbackToDestructiveMigration().build()
}