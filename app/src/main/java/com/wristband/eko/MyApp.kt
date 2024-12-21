package com.wristband.eko

import android.app.Application
import androidx.room.Room
import com.wristband.eko.data.AppDatabase

class MyApp: Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .createFromAsset("database.sqlite").build()
    }

    fun getDatabase(): AppDatabase {
        return database
    }
}