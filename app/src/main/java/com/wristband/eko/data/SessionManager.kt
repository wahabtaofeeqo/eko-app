package com.wristband.eko.data

import android.content.Context
import android.content.SharedPreferences
import com.wristband.eko.entities.Agent

class SessionManager(val context: Context) {

    private var editor: SharedPreferences.Editor? = null
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        const val NAME = "com.wristband.eko.session"
        const val USERNAME = "name"
        const val USERID = "userId"
        const val ROLE = "role"
        const val PLACE = "place"
        const val LOGGEDIN = "loggedIn"
    }

    init {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
    }

    fun login(agent: Agent, place: String) {
        editor?.let {
            it.putInt(USERID, agent.aid)
            it.putString(USERNAME, agent.name)
            it.putString(ROLE, agent.role)
            it.putBoolean(LOGGEDIN, true)
            it.putString(PLACE, place)
            it.commit()
        }
    }

    fun logout() {
        editor?.let {
            it.remove(USERID)
            it.remove(USERNAME)
            it.remove(LOGGEDIN)
            it.commit()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences?.getBoolean(LOGGEDIN, false) ?: false
    }

    fun getName(): String? {
        return sharedPreferences?.getString(USERNAME, null)
    }

    fun getRole(): String? {
        return sharedPreferences?.getString(ROLE, null)
    }

    fun getPlace(): String? {
        return sharedPreferences?.getString(PLACE, "")
    }
}