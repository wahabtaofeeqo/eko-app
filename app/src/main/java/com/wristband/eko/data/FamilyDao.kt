package com.wristband.eko.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wristband.eko.entities.AttendanceWithUser
import com.wristband.eko.entities.Family

@Dao
interface FamilyDao {

    @Query("SELECT * FROM families WHERE fid = :id LIMIT 1")
    fun get(id: Int): Family?

    @Query("SELECT * FROM families ORDER BY fid DESC")
    fun getAll():  DataSource.Factory<Int, Family>

    @Query("SELECT * FROM families WHERE fullname = :name LIMIT 1")
    fun findByName(name: String): Family?

    @Insert
    fun insert(family: Family)

    @Update
    fun update(family: Family)

    @Delete
    fun delete(family: Family)
}