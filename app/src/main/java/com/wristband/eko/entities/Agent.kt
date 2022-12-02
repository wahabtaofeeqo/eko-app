package com.wristband.eko.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wristband.eko.data.Role

@Entity(tableName = "agents")
data class Agent(

    @PrimaryKey(autoGenerate = true)
    var aid: Int = 0,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "username")
    val username: String?,

    @ColumnInfo(name = "password")
    val password: String?,

    @ColumnInfo(name = "role", defaultValue = "User")
    val role: String = Role.USER.name
)