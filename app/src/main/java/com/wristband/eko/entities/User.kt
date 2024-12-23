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

    @ColumnInfo(name = "email")
    val email: String? = null,

    @ColumnInfo(name = "phone")
    val phone: String? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "category")
    val category: String? = null,

    @ColumnInfo(name = "qr_path")
    val path: String? = null,

    @ColumnInfo(name = "organization")
    val organization: String? = null,

    @ColumnInfo(name = "family_id")
    var familyId: Int = 0
)