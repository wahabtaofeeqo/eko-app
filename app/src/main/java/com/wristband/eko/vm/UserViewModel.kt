package com.wristband.eko.vm

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wristband.eko.data.Result
import com.wristband.eko.entities.User
import com.wristband.eko.repositories.UserRepository
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.concurrent.thread

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val repository: UserRepository
    init {
        repository = UserRepository(application);
    }

    val total = MutableLiveData<Int>()
    val users = MutableLiveData<Result<List<User>>>()
    val export = MutableLiveData<Result<Any>>()

    fun loadUsers() {
        thread(start = true) {
            try {
                val data = repository.getAll()
                users.postValue(Result(data, true, "Users"))
            }
            catch (e: Exception) {
                users.postValue(Result(listOf(), false, "Operation not succeeded"))
            }
        }
    }

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

    fun doExport() {

        if(!Environment.isExternalStorageEmulated()) return
        thread(start = true) {
            try {

                //
                val folder: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if(!folder.exists()) folder.mkdir()

                val file = File(folder,"users.csv")
                val printWriter = PrintWriter(FileWriter(file))

                for(user in repository.getAll()) {
                    printWriter.println("${user.uid}, ${user.name}, ${user.code}")
                }
                printWriter.close()

                //
                export.postValue(Result("Done", true, "Exported"))
            }
            catch (e: Exception) {
                export.postValue(Result("Done", false, "Unable to Export Data"))
            }
        }
    }
}