package com.wristband.eko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.wristband.eko.data.Role
import com.wristband.eko.databinding.ActivityRegisterBinding
import com.wristband.eko.entities.Agent
import com.wristband.eko.vm.AgentViewModel
import es.dmoral.toasty.Toasty

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: AgentViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        viewModel = ViewModelProvider(this)[AgentViewModel::class.java]
        initView()

        viewModel.auth.observe(this) { result ->
            if (result == null) return@observe
            if (result.status) {
                binding.name.text?.clear()
                binding.username.text?.clear()
                binding.password.text?.clear()
                Toasty.success(this, result.message).show()
            }
            else
                Toasty.error(this, result.message).show()
        }
    }

    private fun initView() {

        binding.add.setOnClickListener {
            //
            val name = binding.name.text.toString()
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(name.trim().isEmpty()
                || username.trim().isEmpty()
                || password.trim().isEmpty()) return@setOnClickListener

            //
            val agent = Agent(
                name = name, username = username,
                password = password, role = Role.USER.name
            )

            viewModel.register(agent)
        }
    }
}