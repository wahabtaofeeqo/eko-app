package com.wristband.eko

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.adapters.AttendanceAdapter
import com.wristband.eko.adapters.UserAdapter
import com.wristband.eko.databinding.FragmentHomeBinding
import com.wristband.eko.entities.Attendance
import com.wristband.eko.entities.AttendanceWithUser
import com.wristband.eko.vm.SharedViewModel
import es.dmoral.toasty.Toasty

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: AttendanceAdapter
    private lateinit var binding: FragmentHomeBinding

    private val filters = listOf("None", "Registration", "Morning Session", "Afternoon Session", "Evening Session")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AttendanceAdapter()
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.recycler.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)

        //
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filters)
        binding.filterBy.adapter = arrayAdapter
        binding.filterBy.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                load(filters[position])
            }
        }

       //load
        load(filters[0])

        //
        binding.export.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                viewModel.doExport()
            }
            else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        //
        viewModel.export.observe(requireActivity()) { response ->
            if(response == null) return@observe
            if(response.status) {
                Toasty.success(requireContext(), response.message).show()
            }
            else {
                Toasty.error(requireContext(), response.message).show()
            }
        }
    }

    private fun load(filter: String) {
        viewModel.loadAttendance(filter).observe(requireActivity()) { response ->
            if(response == null) return@observe
            adapter.submitList(response)
            if(adapter.itemCount > 0) {
                binding.empty.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }
        }
    }

    //
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it) {
            viewModel.doExport()
        }
        else {
            Toasty.error(requireContext(), "You need to give permission to Export").show()
        }
    }
}