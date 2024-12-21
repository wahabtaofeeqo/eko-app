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
import com.wristband.eko.data.*
import com.wristband.eko.entities.*
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val database: AppDatabase

    private val userDao: UserDao
    private val familyDao: FamilyDao
    private val attendanceDao: AttendanceDao

    init {
        database = (application as MyApp).getDatabase()

        userDao = database.userDao()
        familyDao = database.familyDao()
        attendanceDao = database.attendanceDao()
    }

    val export = MutableLiveData<Result<Any>>()
    val event = MutableLiveData<Result<Event?>>()
    val verification = MutableLiveData<Result<Attendance?>>()
    val families: LiveData<PagedList<Family>> = familyDao.getAll().toLiveData(pageSize = 10)
    val attendances: LiveData<PagedList<AttendanceWithUser>> = attendanceDao.attendanceWithUser().toLiveData(pageSize = 10)

    val familyLink = MutableLiveData<Result<Any?>>()

    private val _addFamily = MutableLiveData<Result<Family?>>()
    val addFamily: LiveData<Result<Family?>> = _addFamily

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

    fun loadAttendance(filter: String): LiveData<PagedList<AttendanceWithUser>> {
        val data = if(filter.lowercase() == "none") database.attendanceDao().attendanceWithUser()
        else database.attendanceDao().attendanceWithUserAndFilter(filter)

        //
        return data.toLiveData(pageSize = 10)
    }

    fun doVerify(code: String, place: String, reason: String) {
        thread(start = true) {

            // Get User
            val user = userDao.findByCode(code.uppercase())
            if(user == null) {
                verification.postValue(Result(null, false, "User does not exist"))
                return@thread
            }
            
            // Check Attendance
            val attendance = attendanceDao.getLastAttendance(user.uid)
            if(attendance == null) {
                attendanceDao.insert(Attendance(user_id = user.uid, reason = reason, date = Date(), place = place))
                verification.postValue(Result(null, true, "Welcome ${user.name}"))
                return@thread
            }

            val todayDate = SimpleDateFormat("MM-dd-yyyy").format(Date())
            val lastAttendance = attendance.date?.let { SimpleDateFormat("MM-dd-yyyy").format(it) }
            if(todayDate == lastAttendance && attendance.place.lowercase() == place.lowercase() && attendance.reason == reason) {
                verification.postValue(Result(null, false, "You already clocked in Today"))
            }
            else {

                //Check Family and Checkout date
                val family: Family?
                if(user.familyId != 0) {
                    family = familyDao.get((user.familyId))

                    if(family != null) {
                        val date = family.checkOut?.let { SimpleDateFormat("MM-dd-yyyy").parse(it) }
                        if(date != null) {
                            if(date.before(Date())) {
                                verification.postValue(Result(null, false, "You already passed your checkout Date"))
                                return@thread
                            }
                        }
                    }
                }

                attendanceDao.insert(Attendance(user_id = user.uid, reason = reason, date = Date(), place = place))
                verification.postValue(Result(null, true, "Welcome"))
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

                var count = 0
                for(user in attendanceDao.getAllAttendanceWithUser()) {
                    count++
                    val date = user.date?.let { SimpleDateFormat("E, dd MMM yyyy H:m a").format(it) }
                    printWriter.println("${count}, ${user.name}, ${user.code}, ${user.place}, ${user.reason}, $date")
                }

                //
                printWriter.close()
                export.postValue(Result("Done", true, "Data exported to Downloads folder"))
            }
            catch (e: Exception) {
                export.postValue(Result("Done", false, "Unable to Export Data"))
            }
        }
    }

    fun createFamily(family: Family) {
        thread (start = true) {

            val familyCheck = familyDao.findByName(family.fullname!!)
            if (familyCheck != null) {
                _addFamily.postValue(Result(null, false, "Family already exist"))
                return@thread
            }

            familyDao.insert(family)
            _addFamily.postValue(Result(family, true, "Operation succeeded"))
        }
    }

    fun linkFamily(familyId: Int, codes: List<String>, shouldCreate: Boolean) {
        thread (start = true) {

            // Check Family
            val family = familyDao.get(familyId)
            if (family == null) {
                familyLink.postValue(Result(null, false, "Operation not succeeded"))
                return@thread
            }

            //
            var linked = false
            var user: User? = null
            for (i in codes) {
                user = userDao.getByCode(i.uppercase())
                if(user != null) {
                    user.familyId = familyId
                    user.name = family.fullname + " Family"
                    userDao.update(user)
                    linked = true
                }
                else {
                    if(shouldCreate) {
                        userDao.insert(User(
                            name = family.fullname,
                            code = i.uppercase(),
                            familyId = family.fid,
                        ))
                    }
                }
            }

            //
            family.linked = linked
            familyDao.update(family)
            familyLink.postValue(Result(user, true, "Operation succeeded"))
        }
    }
}