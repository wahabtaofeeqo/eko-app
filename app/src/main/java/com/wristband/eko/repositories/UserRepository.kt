package com.wristband.eko.repositories

import android.app.Application
import com.wristband.eko.MyApp
import com.wristband.eko.data.UserDao
import com.wristband.eko.entities.User

class UserRepository(private val application: Application) {

    private var dao: UserDao
    init {
        val app: MyApp = application as MyApp
        dao = app.getDatabase().userDao()
    }

    fun getAll(): List<User> {
        return  dao.getAll()
    }

    fun getCount(): Int {
        return  dao.count()
    }
}