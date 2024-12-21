package com.wristband.eko.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wristband.eko.entities.*

@TypeConverters(Converters::class)
@Database(entities = [Agent::class, User::class, Family::class,
    Event::class, Attendance::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "com.wristband.eko.db"
    }

    abstract fun userDao(): UserDao
    abstract fun agentDao(): AgentDao
    abstract fun eventDao(): EventDao
    abstract fun familyDao(): FamilyDao
    abstract fun attendanceDao(): AttendanceDao
}