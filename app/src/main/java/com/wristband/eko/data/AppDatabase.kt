package com.wristband.eko.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wristband.eko.entities.Agent
import com.wristband.eko.entities.Attendance
import com.wristband.eko.entities.Event
import com.wristband.eko.entities.User

@TypeConverters(Converters::class)
@Database(entities = [Agent::class, User::class,
    Event::class, Attendance::class], version = 5)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "com.wristband.eko.db"
    }

    abstract fun userDao(): UserDao
    abstract fun agentDao(): AgentDao
    abstract fun eventDao(): EventDao
    abstract fun attendanceDao(): AttendanceDao
}