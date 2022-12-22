package com.wristband.eko.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wristband.eko.data.Role
import java.util.*

@Entity(tableName = "families")
data class Family(

    @PrimaryKey(autoGenerate = true)
    var fid: Int = 0,

    @ColumnInfo()
    val firstname: String?,

    @ColumnInfo()
    var lastname: String? = null,

    @ColumnInfo()
    var rooms: String? = null,

    @ColumnInfo(name = "room_type")
    var roomsType: String? = null,

    @ColumnInfo(name = "package_type")
    var packageType: String? = null,

    @ColumnInfo(name = "family_size")
    var familySize: Int = 0,

    @ColumnInfo()
    var linked: Boolean = false,

    @ColumnInfo(name = "check_in")
    var checkIn: String? = null,

    @ColumnInfo(name = "check_out")
    var checkOut: String? = null
)