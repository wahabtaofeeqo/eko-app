package com.wristband.eko

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.wristband.eko.databinding.ActivityFamilyLinkBinding
import com.wristband.eko.vm.SharedViewModel
import es.dmoral.toasty.Toasty
import kotlin.concurrent.thread

class FamilyLinkActivity : AppCompatActivity() {

    private var familyID: Int = 0
    val list = mutableListOf<String>()
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var binding: ActivityFamilyLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFamilyLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        familyID = intent.getIntExtra(FamilyFragment.FAMILY_ID, 0)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.listview.adapter = adapter

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        initView()

        thread(familyID != 0) {
            val familyDao = (application as MyApp).getDatabase().familyDao()
            val family = familyDao.get(familyID)
            if(family != null) {
                binding.name.text = "The ${family.fullname} Family"
            }
        }
    }

    private fun initView() {
        binding.add.setOnClickListener {
            val code = binding.code.text.toString()
            if (code.trim().isNotEmpty()) {
                list.add(code)
                adapter.notifyDataSetChanged()
            }
        }

        binding.btn.setOnClickListener {
            if(list.size > 0) {
                viewModel.linkFamily(familyID, list)
            }
        }

        binding.code.addTextChangedListener(textWatcher)
        viewModel.familyLink.observe(this) { response ->
            if (response == null) return@observe

            if (response.status) {
                Toasty.success(this, response.message).show()
            }
            else {
                Toasty.info(this, response.message).show()
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                if(s.trim().length >= 6) {
                    list.add(s.toString())
                    adapter.notifyDataSetChanged()
                    binding.code.text?.clear()
                }
            }
        }
    }
}