package com.example.flightmobileapp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.synthetic.main.activity_main.*
@Entity(tableName = "local_hosts")
data class LocalHost (
    @PrimaryKey var urlAdress: String,
    @ColumnInfo(name = "start_time")
    var startTime: Long = System.currentTimeMillis()

)