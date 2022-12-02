package com.wristband.eko.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    var eid: Int = 0,

    @ColumnInfo(defaultValue = "Eko Hotel and Suit")
    val location: String? = null,

    @ColumnInfo()
    var date: Date?
)
