package com.example.flightmobileapp
import androidx.room.*

@Dao
interface functions {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (host:LocalHost)
    @Query("SELECT DISTINCT * FROM local_hosts")
    fun getLast5 () : List<LocalHost>
}