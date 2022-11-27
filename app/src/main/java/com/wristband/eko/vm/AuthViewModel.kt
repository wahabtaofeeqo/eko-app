package com.wristband.eko.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wristband.eko.data.Result
import com.wristband.eko.entities.Agent
import com.wristband.eko.repositories.AgentRepository
import kotlin.concurrent.thread

class AuthViewModel(application: Application): AndroidViewModel(application) {

    private val repository: AgentRepository
    init {
        repository = AgentRepository(application);
    }

    private var _loggedIn = MutableLiveData<Result<Agent?>>()
    val loggedIn: LiveData<Result<Agent?>> = _loggedIn

    fun login(username: String, password: String) {

        thread(start = true) {
            try {
                val agent = repository.findByUsername(username)
                if(agent != null) {
                    if (password == agent.password) {
                        _loggedIn.postValue(Result(agent, true, "Login Successfully"))
                    }
                    else {
                        _loggedIn.postValue( Result(null, false, "Password not correct"))
                    }
                }
                _loggedIn.postValue( Result(null, false, "Username not correct"))
            }
            catch (e: Exception) {
                _loggedIn.postValue(Result(null, false, "Operation not succeeded"))
            }
        }
    }
}