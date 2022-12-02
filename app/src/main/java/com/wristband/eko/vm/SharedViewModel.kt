package com.wristband.eko.vm

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wristband.eko.MyApp
import com.wristband.eko.data.AppDatabase
import com.wristband.eko.data.AttendanceDao
import com.wristband.eko.data.Result
import com.wristband.eko.data.UserDao
import com.wristband.eko.entities.Attendance
import com.wristband.eko.entities.AttendanceWithUser
import com.wristband.eko.entities.Event
import com.wristband.eko.entities.User
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.concurrent.thread

class SharedViewModel(application: Application): AndroidViewModel(application) {


    private val database: AppDatabase

    private val userDao: UserDao
    private val attendanceDao: AttendanceDao

    init {
        database = (application as MyApp).getDatabase()

        userDao = database.userDao()
        attendanceDao = database.attendanceDao()
    }

    val export = MutableLiveData<Result<Any>>()
    val event = MutableLiveData<Result<Event?>>()
    val attendances = MutableLiveData<Result<List<AttendanceWithUser>>>()
    val verification = MutableLiveData<Result<Attendance?>>()

    fun getEvent() {
        thread(start = true) {
            try {
                val model = database.eventDao().find()
                if(model == null) {
                    event.postValue(Result(null, false, "Event date not set"))
                }
                else {
                    event.postValue(Result(model, true, "Event date"))
                }
            }
            catch (e: Exception) {
                event.postValue(Result(null, false, "Operation not succeeded"))
            }
        }
    }

    fun updateOrCreate(date: Date) {
        thread(start = true) {
            try {
                val dao = database.eventDao()
                val data: Event? = dao.find()
                if(data == null) {
                    dao.insert(Event(date = date))
                }
                else {
                    data.date = date
                    dao.update(data)
                }
                event.postValue(Result(data, true, "Operation succeeded"))
            }
            catch (e: Exception) {
                event.postValue(Result(null, false, "Operation not succeeded"))
            }
        }
    }

    fun loadAttendance() {
        thread(start = true) {
            try {
                val data = database.attendanceDao().attendanceWithUser()
                attendances.postValue(Result(data, true, "Operation succeeded"))
            }
            catch (e: Exception) {
                attendances.postValue(Result(listOf(), false, "Operation not succeeded"))
            }
        }
    }

    fun doVerify(code: String, place: String) {
        thread(start = true) {

            // Get User
            val user = userDao.findByCode(code)
            if(user == null) {
                verification.postValue(Result(null, false, "User does not exist"))
                return@thread
            }

            // Check Attendance
            val attendance = attendanceDao.getLastAttendance(user.uid)
            if(attendance == null) {
                attendanceDao.insert(Attendance(user_id = user.uid, date = Date(), place = place))
                verification.postValue(Result(null, true, "Welcome ${user.name}"))
                return@thread
            }

            val todayDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
            val lastAttendance = attendance.date?.let { SimpleDateFormat("dd-MM-yyyy").format(it) }
            if(todayDate == lastAttendance) {
                verification.postValue(Result(null, false, "You already clocked in Today"))
            }
            else {
                // Check Category
                attendanceDao.insert(Attendance(user_id = user.uid, date = Date(), place = place))
                verification.postValue(Result(null, true, "Welcome ${user.name}"))
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

                val file = File(folder,"attendance.csv")
                val printWriter = PrintWriter(FileWriter(file))

                var count = 1;
                for(user in attendanceDao.attendanceWithUser()) {
                    printWriter.println("${count}, ${user.name}, ${user.code}, ${user.place}")
                    count++
                }

                //
                printWriter.close()
                export.postValue(Result("Done", true, "Exported"))
            }
            catch (e: Exception) {
                export.postValue(Result("Done", false, "Unable to Export Data"))
            }
        }
    }
}