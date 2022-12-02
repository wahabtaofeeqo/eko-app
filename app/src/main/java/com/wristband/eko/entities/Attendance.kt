package com.wristband.eko.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "attendances")
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    var aid: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo()
    var date: Date?
)
