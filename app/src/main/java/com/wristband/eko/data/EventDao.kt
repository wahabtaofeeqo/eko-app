package com.wristband.eko.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wristband.eko.entities.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM events LIMIT 1")
    fun find(): Event?

    @Insert
    fun insert(event: Event)

    @Update
    fun update(event: Event)

    @Delete
    fun delete(event: Event)
}