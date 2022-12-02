package com.wristband.eko.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wristband.eko.data.Result
import com.wristband.eko.entities.Agent
import com.wristband.eko.repositories.AgentRepository
import kotlin.concurrent.thread

class AgentViewModel(application: Application): AndroidViewModel(application) {

    private val repository: AgentRepository
    init {
        repository = AgentRepository(application);
    }

    val auth = MutableLiveData<Result<Agent?>>()
    private var _loggedIn = MutableLiveData<Result<Agent?>>()
    val loggedIn: LiveData<Result<Agent?>> = _loggedIn

    val agents = MutableLiveData<Result<List<Agent>>>()

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
                else {
                    _loggedIn.postValue( Result(null, false, "Username not correct"))
                }
            }
            catch (e: Exception) {
                _loggedIn.postValue(Result(null, false, "Operation not succeeded"))
            }
        }
    }

    fun register(input: Agent) {

        thread(start = true) {
            try {
                // Check
                val agent = repository.findByUsername(input.username.toString())
                if(agent != null) {
                    auth.postValue(Result(agent, false, "Username already taken"))
                }
                else {
                    // Create
                    repository.create(input)
                    auth.postValue(Result(input, true, "Created Successfully"))
                }
            }
            catch (e: Exception) {
                auth.postValue(Result(null, false, "Operation not succeeded"))
            }
        }
    }

    fun loadAgents() {
        thread(start = true) {
            try {
                val data = repository.getAll()
                agents.postValue(Result(data, true, "Agents"))
            }
            catch (e: Exception) {
                agents.postValue(Result(listOf<Agent>(), false, "Operation not succeeded"))
            }
        }
    }
}