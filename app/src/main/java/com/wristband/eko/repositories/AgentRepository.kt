package com.wristband.eko.repositories

import android.app.Application
import androidx.room.Room
import com.wristband.eko.MyApp
import com.wristband.eko.data.AgentDao
import com.wristband.eko.data.AppDatabase
import com.wristband.eko.entities.Agent

class AgentRepository(private val application: Application) {

    private var dao: AgentDao
    init {
        val app: MyApp = application as MyApp
        dao = app.getDatabase().agentDao()
    }

    fun findByUsername(username: String): Agent? {
        return  dao.findByUsername(username)
    }
}