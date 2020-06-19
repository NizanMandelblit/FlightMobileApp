package com.example.flightmobileapp

import androidx.room.*

@Dao
interface SleepDatabaseDao {
    // also works auto-magically: @Update, @Delete
    @Insert
    fun insert(night: SleepNight)

    @Query("SELECT * from daily_sleep_quality_table  WHERE nightId = :key ORDER BY nightId DESC LIMIT 1")
    fun getFirstBy(key: Long): SleepNight?
}