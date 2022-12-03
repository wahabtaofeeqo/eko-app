package com.wristband.eko.vm

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.wristband.eko.MyApp
import com.wristband.eko.data.Result
import com.wristband.eko.data.UserDao
import com.wristband.eko.entities.User
import com.wristband.eko.repositories.UserRepository
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.concurrent.thread

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val dao: UserDao
    private val repository: UserRepository
    init {
        repository = UserRepository(application);
        dao = (application as MyApp).getDatabase().userDao()
    }

    val total = MutableLiveData<Int>()
    val export = MutableLiveData<Result<Any>>()
    val users: LiveData<PagedList<User>> = dao.getAll().toLiveData(pageSize = 10)

    fun getCount() {
        thread(start = true) {
            try {
                val count = repository.getCount()
                total.postValue(count)
            }
            catch (e: Exception) {
                total.postValue(0)
            }
        }
    }
}