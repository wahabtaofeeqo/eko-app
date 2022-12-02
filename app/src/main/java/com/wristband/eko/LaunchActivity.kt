package com.wristband.eko

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wristband.eko.data.Role
import com.wristband.eko.data.SessionManager
import com.wristband.eko.entities.Agent
import kotlin.concurrent.thread

class LaunchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        val sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn()) {
            if(sessionManager.getRole() == Role.ADMIN.name) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        else {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        this.createAdmin()
        finish()
    }

    private fun createAdmin() {
        val dao = (application as MyApp).getDatabase().agentDao()
        thread(start = true) {
            try {
                val admin = dao.findByUsername("admin@yahoo.com")
                if(admin == null) {
                    val agent = Agent(
                        name = "Admin",
                        username = "admin@yahoo.com",
                        password = "password",
                        role = Role.ADMIN.name)
                    dao.insert(agent)
                }
            }
            catch (e: Exception) {
                //
            }
        }
    }
}