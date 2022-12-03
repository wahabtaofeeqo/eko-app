package com.wristband.eko.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wristband.eko.entities.User
import com.wristband.eko.entities.UserWithAttendances

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): DataSource.Factory<Int, User>

    @Query("SELECT * FROM users")
    fun loadAll(): List<User>

    @Query("SELECT COUNT(*) FROM users")
    fun count(): Int

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users WHERE uid = :id")
    fun loadUserAttendance(id: Int): List<UserWithAttendances>

    @Query("SELECT * FROM users WHERE code = :code")
    fun findByCode(code: String): User?
}