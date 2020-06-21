package com.example.flightmobileapp

import android.app.Application
import android.content.Context
//import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [LocalHost::class])
abstract class UrlDatabase : RoomDatabase() {
    /*
    var a=get (application)
    companion object {
        fun get(application: Application): UrlDatabase {
            return Room.databaseBuilder(application, UrlDatabase::class.java, "urlDataBase")
                .allowMainThreadQueries().build()
        }

        @Volatile
        private var instance: UrlDatabase? = null
        fun getInstance(context: Context): UrlDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): UrlDatabase {
            return Room.databaseBuilder(context, UrlDatabase::class.java, "DATABASE")
                .build()
        }

        fun get(context: Context): UrlDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                UrlDatabase::class.java,
                "urlDataBase"
            )
                .fallbackToDestructiveMigration().build()
        }

    }
*/
    abstract val functionsDatabaseDao: Functions

    companion object {
        @Volatile
        private var INSTANCE: UrlDatabase? = null
        fun getInstance(context: Context): UrlDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UrlDatabase::class.java,
                        "url_database")
                        .fallbackToDestructiveMigration().build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }

    //abstract fun getDao2(): Functions


}