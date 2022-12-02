package com.wristband.eko

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.wristband.eko.adapters.AgentAdapter
import com.wristband.eko.adapters.UserAdapter
import com.wristband.eko.databinding.FragmentUsersBinding
import com.wristband.eko.entities.User
import com.wristband.eko.vm.SharedViewModel
import com.wristband.eko.vm.UserViewModel
import es.dmoral.toasty.Toasty

/**
 * A simple [Fragment] subclass.
 * Use the [AttendanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {

    private var list = mutableListOf<User>()

    lateinit var adapter: UserAdapter
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = UsersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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

    private fun initView() {
        viewModel.loadUsers()
        adapter = UserAdapter(requireActivity().applicationContext, list)

        //
        binding.recycler.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)

        //
        viewModel.users.observe(requireActivity()) { response ->
            if(response == null) return@observe
            list.addAll(response.body)
            if(list.size > 0) {
                binding.empty.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }

            adapter.notifyDataSetChanged()
        }

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
}