package com.wristband.eko.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wristband.eko.entities.Agent

@Database(entities = [Agent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "com.wristband.eko.db"
    }

    abstract fun agentDao(): AgentDao
}