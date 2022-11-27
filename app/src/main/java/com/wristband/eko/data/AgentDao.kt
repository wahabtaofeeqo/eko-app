package com.wristband.eko.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wristband.eko.entities.Agent

@Dao
interface AgentDao {

    @Query("SELECT * FROM agents")
    fun getAll(): List<Agent>

    @Query("SELECT * FROM agents WHERE username = :username LIMIT 1")
    fun findByUsername(username: String): Agent

    @Insert
    fun insert(agent: Agent)

    @Delete
    fun delete(agent: Agent)
}