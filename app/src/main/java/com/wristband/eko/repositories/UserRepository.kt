package com.wristband.eko.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.wristband.eko.MyApp
import com.wristband.eko.data.UserDao
import com.wristband.eko.entities.User

class UserRepository(private val application: Application) {

    private var dao: UserDao
    init {
        val app: MyApp = application as MyApp
        dao = app.getDatabase().userDao()
    }

    fun loadAll(): LiveData<PagedList<User>> {
        return dao.getAll().toLiveData(pageSize = 10)
    }

    fun getCount(): Int {
        return  dao.count()
    }
}