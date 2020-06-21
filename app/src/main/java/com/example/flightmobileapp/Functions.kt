package com.example.flightmobileapp
import androidx.room.*

@Dao
interface Functions {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert1 (host:LocalHost)
    @Query("SELECT DISTINCT * FROM local_hosts ORDER BY start_time DESC LIMIT 5")
    fun getLast5 () : List<LocalHost>
}