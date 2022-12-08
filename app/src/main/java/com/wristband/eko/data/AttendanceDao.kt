package com.wristband.eko.data

import androidx.paging.DataSource
import androidx.room.*
import com.wristband.eko.entities.Attendance
import com.wristband.eko.entities.AttendanceWithUser

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM attendances")
    fun findAll(): List<Attendance>

    @Query("SELECT * FROM attendances WHERE user_id = :userId ORDER BY aid DESC LIMIT 1")
    fun getLastAttendance(userId: Int): Attendance?

    @Query("SELECT * FROM attendances INNER JOIN users ON users.uid = user_id")
    fun attendanceWithUser(): DataSource.Factory<Int, AttendanceWithUser>

    @Query("SELECT * FROM attendances INNER JOIN users ON users.uid = user_id")
    fun getAllAttendanceWithUser(): List<AttendanceWithUser>

    @Query("SELECT * FROM attendances INNER JOIN users ON users.uid = user_id WHERE place = :filter")
    fun attendanceWithUserAndFilter(filter: String):  DataSource.Factory<Int, AttendanceWithUser>

    @Insert
    fun insert(attendance: Attendance)

    @Delete
    fun delete(attendance: Attendance)
}