package com.wristband.eko.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithAttendances(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "uid",
        entityColumn = "user_id")
    val list: List<Attendance>
)