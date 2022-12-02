package com.wristband.eko.entities

import androidx.room.ColumnInfo
import java.util.Date

data class AttendanceWithUser(
    @ColumnInfo(name = "aid")
    val aid: Int?,

    @ColumnInfo(name = "user_id")
    val user_id: Int?,

    @ColumnInfo(name = "date")
    val date: Date?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "code")
    val code: String?,

    @ColumnInfo(name = "place")
    val place: String?,

    @ColumnInfo(name = "category")
    val category: String?,
)