package com.wristband.eko.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "code")
    val code: String?,

    @ColumnInfo(name = "category")
    val category: String? = null,

    @ColumnInfo(name = "family_id")
    var familyId: Int = 0
)